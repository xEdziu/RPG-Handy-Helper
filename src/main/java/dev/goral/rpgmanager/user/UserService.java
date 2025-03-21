package dev.goral.rpgmanager.user;

import dev.goral.rpgmanager.security.CustomReturnables;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static dev.goral.rpgmanager.user.register.RegisterService.validatePassword;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "Nie znaleziono użytkownika o nicku %s";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Logowanie użytkowników przez Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));

        if (user.getPassword() == null) {
            throw new IllegalStateException("To konto zostało utworzone przez Discord. Musisz najpierw ustawić hasło.");
        }

        return user;
    }

    public UserDTO getAuthorizedUser() {
        Object principal = getAuthentication().getPrincipal();

        if (principal instanceof User foundUser) {
            return new UserDTO(foundUser.getUsername(), foundUser.getFirstName(), foundUser.getSurname(), foundUser.getEmail());
        } else if (principal instanceof DefaultOAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return new UserDTO(user.getUsername(), user.getFirstName(), user.getSurname(), user.getEmail());
            } else {
                throw new IllegalStateException("Nie udało się znaleźć użytkownika w bazie danych.");
            }
        }
        throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Map<String, Object> setPassword(String password) {

        Object principal = getAuthentication().getPrincipal();

        if (!validatePassword(password)) {
            throw new IllegalStateException("Hasło musi zawierać co najmniej 8 znaków, jedną cyfrę, jedną małą literę, jedną dużą literę oraz jeden znak specjalny.");
        }

        if (principal instanceof User foundUser) {
            if (foundUser.getPassword() != null) {
                throw new IllegalStateException("Hasło jest już ustawione. Użyj opcji resetowania hasła.");
            }
            foundUser.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(foundUser);
        } else if (principal instanceof DefaultOAuth2User DefaultOAuth2User) {
            User user = userRepository.findByOAuthId(DefaultOAuth2User.getAttribute("id"))
                    .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika powiązanego z tym kontem Discord"));

            if (user.getPassword() != null) {
                throw new IllegalStateException("Hasło jest już ustawione. Użyj opcji resetowania hasła.");
            }
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(user);
        }

        return CustomReturnables.getOkResponseMap("Hasło zostało ustawione");
    }

    public Map<String, Object> setUserPhoto(String userPhotoPath) {
        //TODO: Sprawdzić czy ścieżka jest poprawna
        User user = (User) getAuthentication().getPrincipal();
        user.setUserPhotoPath(userPhotoPath);
        userRepository.save(user);
        return CustomReturnables.getOkResponseMap("Zdjęcie profilowe zostało ustawione.");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Map<String, Object> createUserAdmin(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Użytkownik o podanym nicku już istnieje.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Użytkownik o podanym emailu już istnieje.");
        }
        if (userRepository.findByOAuthId(user.getOAuthId()).isPresent()) {
            throw new IllegalStateException("Użytkownik o podanym ID OAuth już istnieje.");
        }

        userRepository.save(user);
        return CustomReturnables.getOkResponseMap("Użytkownik został utworzony.");
    }

}

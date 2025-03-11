package dev.goral.rpgmanager.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.goral.rpgmanager.email.EmailService;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "Nie znaleziono użytkownika o nicku %s";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;

    /**
     * Rejestracja użytkownika przez e-mail
     */
    @Transactional
    public ResponseEntity<String> registerUser(String username, String firstName, String surname, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Nazwa użytkownika jest już zajęta.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Ten adres e-mail jest już zarejestrowany.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);
        String verificationToken = UUID.randomUUID().toString();

        User newUser = new User(username, firstName, surname, email, encodedPassword, UserRole.ROLE_USER, new Timestamp(System.currentTimeMillis()));
        newUser.setEnabled(false); // Konto nieaktywne do momentu potwierdzenia e-maila
        OAuthProvider emailProvider = OAuthProvider.EMAIL;
        newUser.setOAuthProvider(emailProvider);
        newUser.setToken(verificationToken);

        userRepository.save(newUser);
        emailService.sendVerificationEmail(newUser); // Wysyłanie maila z tokenem

        return ResponseEntity.ok("Konto zostało utworzone. Sprawdź swoją skrzynkę e-mail, aby aktywować konto.");
    }

    /**
     * Potwierdzanie adresu e-mail za pomocą tokena
     */
    public ResponseEntity<String> confirmEmail(String token) {
        Optional<User> userOptional = userRepository.findByToken(token);

        if (userOptional.isEmpty()) {
            throw new IllegalStateException("Nieprawidłowy lub nieaktywny token weryfikacyjny");
        }

        User user = userOptional.get();
        user.setEnabled(true);
        user.setToken(null); // Usuwamy token po weryfikacji
        userRepository.save(user);

        return ResponseEntity.ok("Twoje konto zostało aktywowane!");
    }

    /**
     * Logowanie użytkowników przez Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
    }

    /**
     * Obsługa logowania przez Discord OAuth2
     */
    @Transactional
    public User processOAuthPostLogin(String oauthId, String email, String username) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            OAuthProvider discordProvider = OAuthProvider.DISCORD;
            user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setOAuthProvider(discordProvider);
            user.setOAuthId(oauthId);
            user.setRole(UserRole.ROLE_USER);
            user.setEnabled(true);
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
        }
        return user;
    }

    public UserDTO getAuthorizedUser() {
        Object principal = getAuthentication().getPrincipal();

        if (principal instanceof User foundUser) {
            // Logowanie tradycyjne (e-mail i hasło)
            return new UserDTO(foundUser.getUsername(), foundUser.getFirstName(), foundUser.getSurname(), foundUser.getEmail());
        } else if (principal instanceof DefaultOAuth2User oauthUser) {
            // Logowanie przez Discord OAuth2
            String email = oauthUser.getAttribute("email");
            String username = oauthUser.getAttribute("username");
            return new UserDTO(username, "", "", email); // Discord nie zwraca firstName i surname
        }

        throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika");
    }


    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

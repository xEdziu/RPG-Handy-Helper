package dev.goral.rpgmanager.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "Nie znaleziono użytkownika o nicku %s";
    private final UserRepository userRepository;


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

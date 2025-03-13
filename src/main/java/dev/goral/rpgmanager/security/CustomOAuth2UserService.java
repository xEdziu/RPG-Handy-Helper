package dev.goral.rpgmanager.security;

import dev.goral.rpgmanager.user.OAuthProvider;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import dev.goral.rpgmanager.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final Random random = new Random();

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthId = oAuth2User.getAttribute("id"); // Unikalne ID użytkownika Discorda
        String email = oAuth2User.getAttribute("email");
        String username = oAuth2User.getAttribute("username");

        Optional<User> existingUserByEmail = userRepository.findByEmail(email);
        Optional<User> existingUserByOAuthId = userRepository.findByOAuthId(oauthId);
        User user;

        if (existingUserByOAuthId.isPresent()) {
            // Użytkownik już logował się przez Discord - aktualizujemy jego dane (np. nazwę użytkownika)
            user = existingUserByOAuthId.get();

            if (!user.getUsername().equals(username)) {
                if (userRepository.findByUsername(username).isPresent()) {
                    username = generateUniqueUsername(username);
                }
                user.setUsername(username);
                userRepository.save(user);
            }
        } else if (existingUserByEmail.isPresent()) {
            // Użytkownik ma już konto w bazie przez e-mail, ale loguje się pierwszy raz przez Discord -> Powiązanie konta!
            user = existingUserByEmail.get();
            user.setOAuthId(oauthId); // Przypisujemy jego Discord ID
            user.setOAuthProvider(OAuthProvider.DISCORD);
            userRepository.save(user);
        } else {
            // Użytkownik loguje się pierwszy raz przez Discord i nie ma konta w bazie
            if (userRepository.findByUsername(username).isPresent()) {
                username = generateUniqueUsername(username);
            }

            user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setOAuthProvider(OAuthProvider.DISCORD);
            user.setOAuthId(oauthId);
            user.setRole(UserRole.ROLE_USER);
            user.setEnabled(true);
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
        }

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                oAuth2User.getAttributes(),
                "id");
    }

    /**
     * Generowanie unikalnej nazwy użytkownika
     * @param baseUsername nazwa użytkownika
     * @return nowa, unikalna nazwa użytkownika
     */
    private String generateUniqueUsername(String baseUsername) {
        String newUsername;
        do {
            newUsername = baseUsername + random.nextInt(10000, 99999);
        } while (userRepository.findByUsername(newUsername).isPresent());
        return newUsername;
    }
}

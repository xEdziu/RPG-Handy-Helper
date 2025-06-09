package dev.goral.rpghandyhelper.mobile;

import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@AllArgsConstructor
public class MobileAuthTools {

    static UserRepository userRepository;

    protected static User getUserFromJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof Jwt jwt) {
            String username = jwt.getClaimAsString("sub");
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika: " + username));
        } else {
            throw new IllegalStateException("User not found in JWT");
        }
    }
}

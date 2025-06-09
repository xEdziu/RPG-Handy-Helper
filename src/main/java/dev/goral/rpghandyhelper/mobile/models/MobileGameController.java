package dev.goral.rpghandyhelper.mobile.models;

import dev.goral.rpghandyhelper.game.GameService;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import dev.goral.rpghandyhelper.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/mobile/v1/authorized")
public class MobileGameController {

    UserService userService;
    UserRepository userRepository;
    GameService gameService;

    @GetMapping(path = "/game/userGames")
    public Map<String, Object> getUserGames() {
        User currentUser = getUserFromJwt();
        return gameService.getUserGames(currentUser);
    }

    @GetMapping(path = "/game/getSystemBy")

    private User getUserFromJwt() {
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

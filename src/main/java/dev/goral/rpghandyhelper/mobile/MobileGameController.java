package dev.goral.rpghandyhelper.mobile;

import dev.goral.rpghandyhelper.game.GameService;
import dev.goral.rpghandyhelper.user.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/mobile/v1/authorized")
public class MobileGameController {

    GameService gameService;
    MobileAuthTools mobileAuthTools;

    @GetMapping(path = "/game/userGames")
    public Map<String, Object> getUserGames() {
        User currentUser = mobileAuthTools.getUserFromJwt();
        return gameService.getUserGamesWithPlayers(currentUser);
    }
}

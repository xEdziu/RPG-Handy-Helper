package dev.goral.rpgmanager.game;

import dev.goral.rpgmanager.game.gameUsers.*;
import dev.goral.rpgmanager.user.User;
import lombok.AllArgsConstructor;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class GameController {

    private final GameService gameService;

    // ============ User methods ============

    @GetMapping(path = "/game/{gameId}")
    public Map<String, Object> getGame(@PathVariable("gameId") Long gameId) {
        return gameService.getGame(gameId);
    }

    @GetMapping(path = "/game/allPlayers/{gameId}")
    public Map<String, Object> getGamePlayers(@PathVariable("gameId") Long gameId) {
        return gameService.getGamePlayers(gameId);
    }

    @GetMapping(path = "/game/userGames")
    public Map<String, Object> getUserGames(@AuthenticationPrincipal User currentUser) {
        return gameService.getUserGames(currentUser);
    }

    @PostMapping(path = "/game/create")
    public Map<String, Object> createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    @PostMapping(path = "/game/addUserToGame")
    public Map<String, Object> addUserToGame(@RequestBody AddUserToGameRequest request) {
        return gameService.addUserToGame(request);
    }

    @DeleteMapping(path = "/game/deleteUserFromGame")
    public Map<String, Object> deleteUserToGame(@RequestBody DeleteUserFromGameRequest request) {
        return gameService.deleteUserFromGame(request);
    }


    @PutMapping(path = "/game/updateGame/{gameId}")
    public Map<String, Object> updateGame(@PathVariable("gameId") Long gameId, @RequestBody Game game) {
        return gameService.updateGame(gameId, game);
    }

    @PutMapping(path = "/game/changeStatus/{gameId}")
    public Map<String, Object> changeGameStatus(@PathVariable("gameId") Long gameId, @RequestBody Map<String, String> request) {
        return gameService.changeGameStatus(gameId, request);
    }

    @PutMapping(path = "/game/updateGameUserRole/{gameUserId}")
    public Map<String, Object> updateGameUserRole(@PathVariable("gameUserId") Long gameUserId, @RequestBody Map<String, String> request) {
        return gameService.updateGameUserRole(gameUserId, request);
    }

    // ============ Admin methods ============

    @GetMapping(path = "/admin/game/all")
    public Map<String, Object> getAllGames() {
        return gameService.getAllGames();
    }

}
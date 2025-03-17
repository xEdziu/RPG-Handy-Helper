package dev.goral.rpgmanager.game;

import dev.goral.rpgmanager.game.gameUsers.AddUserToGameRequest;
import dev.goral.rpgmanager.game.gameUsers.GameUsers;
import dev.goral.rpgmanager.game.gameUsers.GameUsersDTO;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // ============ User methods ============

    @GetMapping(path = "/game/{gameId}")
    public GameDTO getGame(@PathVariable("gameId") Long gameId) {
        return gameService.getGame(gameId);
    }

    @GetMapping(path = "/game/allPlayers/{gameId}")
    public List<GameUsersDTO> getGamePlayers(@PathVariable("gameId") Long gameId) {
        return gameService.getGamePlayers(gameId);
    }

    @PostMapping(path = "/game/create")
    public Map<String, Object> createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    @PostMapping(path = "/game/addUserToGame")
    public Map<String, Object> addUserToGame(@RequestBody AddUserToGameRequest request) {
        return gameService.addUserToGame(request);
    }

    @PutMapping(path = "/game/updateGame/{gameId}")
    public Map<String, Object> updateGame(@PathVariable("gameId") Long gameId, @RequestBody Game game) {
        return gameService.updateGame(gameId, game);
    }

    @PutMapping(path = "/game/updateGameUserRole/{gameUserId}")
    public Map<String, Object> updateGameUserRole(@PathVariable("gameUserId") Long gameUserId, @RequestBody Map<String, String> request) {
        return gameService.updateGameUserRole(gameUserId, request);
    }

    // ============ Admin methods ============

    @GetMapping(path = "/admin/game/all")
    public List<GameDTO> getAllGames() {
        return gameService.getAllGames();
    }

}

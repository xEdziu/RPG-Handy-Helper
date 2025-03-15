package dev.goral.rpgmanager.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized/game")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // ============ User methods ============

    @GetMapping(path = "/{gameId}")
    public GameDTO getGame(@PathVariable("gameId") Long gameId) {
        return gameService.getGame(gameId);
    }

    @PostMapping(path = "/createGame")
    public Map<String, Object> createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    @PutMapping(path = "/updateGame/{gameId}")
    public Map<String, Object> updateGame(@PathVariable("gameId") Long gameId, @RequestBody Game game) {
        return gameService.updateGame(gameId, game);
    }

    // ============ Admin methods ============

    @GetMapping(path = "/admin/all")
    public List<GameDTO> getAllGames() {
        return gameService.getGames();
    }

}

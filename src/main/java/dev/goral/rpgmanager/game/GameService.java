package dev.goral.rpgmanager.game;

import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import dev.goral.rpgmanager.user.User;

@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<GameDTO> getGames() {
        return gameRepository.findAll()
                .stream()
                .map(game -> new GameDTO(
                        game.getId(),
                        game.getName(),
                        game.getDescription(),
                        game.getGameMaster().getUsername()
                ))
                .collect(Collectors.toList());
    }

    public GameDTO getGame(Long gameId) {
        return gameRepository.findGameById(gameId)
                .map(game -> new GameDTO(
                        game.getId(),
                        game.getName(),
                        game.getDescription(),
                        game.getGameMaster().getUsername()
                ))
                .orElse(null);
    }

    public Map<String, Object> createGame(Game game) {

        //Można dodać walidacje
        gameRepository.save(game);

        return CustomReturnables.getOkResponseMap("Gra została utworzona.");
    }

    public Map<String, Object> updateGame(Long gameId, Game game) {
        Game gameToUpdate = gameRepository.findGameById(gameId).orElse(null);

        if (gameToUpdate == null) {
            throw new ResourceNotFoundException("Gra o podanym ID nie istnieje.");
        }
        if (game.getName() != null ) {
            if(gameToUpdate.getName().isEmpty()) {
                throw new IllegalArgumentException("Nazwa gry nie może być pusta.");
            }
        }
        if (game.getGameMaster() != null ) {
            if(gameToUpdate.getGameMaster().getUsername().isEmpty()) {
                throw new IllegalArgumentException("Gra musi mieć GameMastera.");
            }
        }

        gameToUpdate.setName(game.getName());
        gameToUpdate.setDescription(game.getDescription());
        gameToUpdate.setGameMaster(game.getGameMaster());
        gameRepository.save(gameToUpdate);

        return CustomReturnables.getOkResponseMap("Gra została zaktualizowana.");
    }


}

package dev.goral.rpgmanager.game;

import dev.goral.rpgmanager.game.gameUsers.*;
import dev.goral.rpgmanager.rpgSystems.RpgSystemsRepository;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameUsersRepository gameUsersRepository;
    private final RpgSystemsRepository rpgSystemsRepository;

    @Autowired
    public GameService(GameRepository gameRepository, UserRepository userRepository, GameUsersRepository gameUsersRepository, RpgSystemsRepository rpgSystemsRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameUsersRepository = gameUsersRepository;
        this.rpgSystemsRepository = rpgSystemsRepository;
    }

    public List<GameDTO> getAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(game -> new GameDTO(
                        game.getId(),
                        game.getName(),
                        game.getDescription(),
                        game.getGameMaster().getUsername(),
                        game.getRpgSystem().getName()
                ))
                .collect(Collectors.toList());
    }

    public GameDTO getGame(Long gameId) {
        return gameRepository.findGameById(gameId)
                .map(game -> new GameDTO(
                        game.getId(),
                        game.getName(),
                        game.getDescription(),
                        game.getGameMaster().getUsername(),
                        game.getRpgSystem().getName()
                ))
                .orElse(null);
    }
    
    public List<GameUsersDTO> getGamePlayers(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o podanym ID nie istnieje."));

        List<GameUsers> gameUsers = gameUsersRepository.findGameAllUsersByGameId(gameId);
        return gameUsers.stream()
                .map(gameUser -> new GameUsersDTO(
                        gameUser.getId(),
                        gameUser.getUser().getId(),
                        gameUser.getGame().getId(),
                        gameUser.getRole().toString()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> createGame(Game game) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        game.setGameMaster(currentUser);
        gameRepository.save(game);

        // Add the creator as a GameMaster to the game
        GameUsers gameUsers = new GameUsers();
        gameUsers.setUser(currentUser);
        gameUsers.setGame(game);
        gameUsers.setRole(GameUsersRole.GAMEMASTER);
        gameUsersRepository.save(gameUsers);

        return CustomReturnables.getOkResponseMap("Gra została utworzona.");
    }

    public Map<String, Object> addUserToGame(AddUserToGameRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Użytkownik o podanym ID nie istnieje."));
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o podanym ID nie istnieje."));

        //TODO: Check if user is already in the game
        //TODO: Sprwadź czy gm dodaje użytkownika do swojej gry

        GameUsers gameUsers = new GameUsers();
        gameUsers.setUser(user);
        gameUsers.setGame(game);
        gameUsers.setRole(GameUsersRole.valueOf(request.getRole()));

        gameUsersRepository.save(gameUsers);
        return CustomReturnables.getOkResponseMap("Użytkownik został dodany do gry.");
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
        gameToUpdate.setRpgSystem(game.getRpgSystem());
        gameRepository.save(gameToUpdate);

        return CustomReturnables.getOkResponseMap("Gra została zaktualizowana.");
    }


}

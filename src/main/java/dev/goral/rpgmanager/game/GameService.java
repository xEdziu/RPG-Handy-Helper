package dev.goral.rpgmanager.game;

import dev.goral.rpgmanager.chat.GameRoomManager;
import dev.goral.rpgmanager.game.gameUsers.*;
import dev.goral.rpgmanager.rpgSystems.RpgSystemsRepository;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ForbiddenActionException;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameUsersRepository gameUsersRepository;
    private final RpgSystemsRepository rpgSystemsRepository;
    private final GameRoomManager gameRoomManager;

    public List<GameDTOAdmin> getAllGames() {
        List<Game> games = gameRepository.findAll();
        return games.stream()
                .map(game -> new GameDTOAdmin(
                        game.getId(),
                        game.getName(),
                        game.getDescription(),
                        game.getGameMaster().getId(),
                        game.getRpgSystem().getId(),
                        game.getStatus().toString()
                ))
                .collect(Collectors.toList());
    }

    public GameDTO getGame(Long gameId) {
        return gameRepository.findGameById(gameId)
                .map(game -> new GameDTO(
                        game.getId(),
                        game.getName(),
                        game.getDescription(),
                        game.getGameMaster().getId(),
                        game.getRpgSystem().getId()
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

        if (game == null || game.getName() == null || game.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Gra musi mieć nazwę.");
        }

        // Check game name
        String name = game.getName().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Niewłaściwa nazwa gry");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Nazwa gry nie może mieć więcej niż 255 znaków.");
        }

        // Check game description
        if (game.getDescription() != null && game.getDescription().length() > 500) {
            throw new IllegalArgumentException("Opis gry nie może mieć więcej niż 500 znaków.");
        }

        if(game.getRpgSystem() == null) {
            throw new IllegalArgumentException("Gra musi mieć przypisany system RPG.");
        }
        if(!rpgSystemsRepository.existsById(game.getRpgSystem().getId())){
            throw new IllegalArgumentException("Podany system RPG nie istnieje.");
        }

        game.setName(name);
        game.setGameMaster(currentUser);
        game.setStatus(GameStatus.valueOf("ACTIVE"));
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

        if ( request.getGameId() == null || request.getUserId() == null || request.getRole() == null) {
            throw new IllegalArgumentException("Nie podano wszystkich danych.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Użytkownik o podanym ID nie istnieje."));
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o podanym ID nie istnieje."));

        if(gameUsersRepository.existsByUserIdAndGameId(user.getId(), game.getId())) {
            throw new IllegalArgumentException("Użytkownik jest już w grze.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        if (!game.getGameMaster().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Tylko GameMaster może dodawać użytkowników do swojej gry.");
        }

        GameUsers gameUsers = new GameUsers();
        gameUsers.setUser(user);
        gameUsers.setGame(game);
        gameUsers.setRole(GameUsersRole.valueOf(request.getRole()));

        gameUsersRepository.save(gameUsers);
        return CustomReturnables.getOkResponseMap("Użytkownik został dodany do gry.");
    }

    public Map<String, Object> deleteUserFromGame(DeleteUserFromGameRequest request) {

        if ( request.getGameId() == null || request.getUserId() == null) {
            throw new IllegalArgumentException("Nie podano wszystkich danych.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Użytkownik o podanym ID nie istnieje."));
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o podanym ID nie istnieje."));

        if(!gameUsersRepository.existsByUserIdAndGameId(user.getId(), game.getId())) {
            throw new IllegalArgumentException("Nie ma użytkownika w grze.");
        }

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalArgumentException("Nie można usunąć użytkownika z nieaktywnej gry.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        GameUsers gameUser = gameUsersRepository.findByGameIdAndUserId(request.getGameId(), currentUser.getId());
        if (gameUser == null || gameUser.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalArgumentException("Nie możesz usunąć użytkownika z gry, gdy nie jesteś jej GameMasterem.");
        }

        GameUsers delUser = gameUsersRepository.findByGameIdAndUserId(request.getGameId(), request.getUserId());
        if(delUser == null||delUser.getRole()==GameUsersRole.GAMEMASTER) {
            throw new IllegalArgumentException("Nie można usunąć GameMastera z gry.");
        }
        gameUsersRepository.delete(delUser);
        return CustomReturnables.getOkResponseMap("Użytkownik został usunięty z gry.");
    }

    @Transactional
    public Map<String, Object> updateGame(Long gameId, Game game) {
        Game gameToUpdate = gameRepository.findGameById(gameId).orElse(null);

        if (gameToUpdate == null) {
            throw new ResourceNotFoundException("Gra o podanym ID nie istnieje.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdzenie, czy na pewno to GM próbuje zmienić swoja gre
        if(!currentUser.getId().equals(gameToUpdate.getGameMaster().getId())) {
            throw new IllegalArgumentException("Nie możesz zmienić gry gdy nie jesteś jej GameMasterem.");
        }

        if(game.getName() == null && game.getGameMaster() == null && game.getDescription() == null) {
            throw new IllegalArgumentException("Brak danych do aktualizacji.");
        }

        if (game.getName() != null) {
            String newName = game.getName().trim();
            if (!newName.isEmpty()) {
                if(gameToUpdate.getName().isEmpty()) {
                    throw new IllegalArgumentException("Nazwa gry nie może być pusta.");
                }
                if (newName.length() > 255) {
                    throw new IllegalArgumentException("Nazwa nie może mieć więcej niż 255 znaków.");
                }
                gameToUpdate.setName(game.getName());
            }
        }
        
        if (game.getGameMaster() != null ) {

            if(gameToUpdate.getGameMaster().getId() == null) {
                throw new IllegalArgumentException("Gra musi mieć GameMastera.");
            }
            if(!userRepository.existsById(game.getGameMaster().getId())){
                throw new ResourceNotFoundException("GameMaster o id " + game.getGameMaster().getId() +" nie istnieje.");
            }

            if(!gameUsersRepository.existsByUserIdAndGameId(game.getGameMaster().getId(), gameId)){
                AddUserToGameRequest addUserToGameRequest = new AddUserToGameRequest();
                addUserToGameRequest.setUserId(game.getGameMaster().getId());
                addUserToGameRequest.setGameId(gameId);
                addUserToGameRequest.setRole(GameUsersRole.GAMEMASTER.toString());
                addUserToGame(addUserToGameRequest);
            }
            Map<String, String> roleUpdateGameMaster = Map.of("role", "GAMEMASTER");
            Map<String, String> roleUpdatePlayer = Map.of("role", "PLAYER");

            updateGameUserRole(gameUsersRepository.findIdByUserIdAndGameId(game.getGameMaster().getId(), gameId), roleUpdateGameMaster);
            updateGameUserRole(gameUsersRepository.findIdByUserIdAndGameId(gameToUpdate.getGameMaster().getId(), gameId), roleUpdatePlayer);

            gameToUpdate.setGameMaster(game.getGameMaster());
        }

        if (game.getDescription() != null ) {
            if (game.getDescription().length() > 500) {
                throw new IllegalArgumentException("Opis gry nie może mieć więcej niż 500 znaków.");
            }
            gameToUpdate.setDescription(game.getDescription());
        }

        gameRepository.save(gameToUpdate);

        return CustomReturnables.getOkResponseMap("Gra została zaktualizowana.");
    }

    @Transactional
    public Map<String, Object> changeGameStatus(Long gameId, Map<String, String> request) {
        String statusStr = request.get("status");

        Game gameToUpdate = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o podanym ID nie istnieje."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdzenie, czy na pewno to GM próbuje zmienić swoja gre
        if(!currentUser.getId().equals(gameToUpdate.getGameMaster().getId())) {
            throw new IllegalArgumentException("Nie możesz zmienić gry gdy nie jesteś jej GameMasterem.");
        }

        if (statusStr == null) {
            throw new IllegalArgumentException("Brak statusu w żądaniu.");
        }

        GameStatus status;
        try {
            status = GameStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Niepoprawny status: " + statusStr);
        }

        List<Map<String, Object>> activeRooms = gameRoomManager.getActiveRoomsForGame(gameId);

        if (!activeRooms.isEmpty())
            throw new ForbiddenActionException("Nie można zmienić statusu gry, gdy istnieją w niej aktywne pokoje.");

        gameToUpdate.setStatus(status);
        gameRepository.save(gameToUpdate);

        return CustomReturnables.getOkResponseMap("Status gry został zaktualizowany.");
    }

    public Map<String, Object> updateGameUserRole(Long gameUserId, Map<String, String> request) {
        String roleStr = request.get("role");

        if (roleStr == null) {
            throw new IllegalArgumentException("Brak roli w żądaniu.");
        }

        GameUsersRole newRole;
        try {
            newRole = GameUsersRole.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Niepoprawna rola: " + roleStr);
        }

        GameUsers gameUserToUpdate = gameUsersRepository.findById(gameUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Użytkownik gry o podanym ID nie istnieje."));

        GameUsersRole currentRole = gameUserToUpdate.getRole();
        Long gameId = gameUserToUpdate.getGame().getId();

        if (currentRole == GameUsersRole.GAMEMASTER && newRole != GameUsersRole.GAMEMASTER) {
            long gameMasterCount = gameUsersRepository.countByGameIdAndRole(gameId, GameUsersRole.GAMEMASTER);
            if (gameMasterCount <= 1) {
                throw new IllegalArgumentException("Nie można usunąć ostatniego GameMastera z gry.");
            }
        }

        gameUserToUpdate.setRole(newRole);
        gameUsersRepository.save(gameUserToUpdate);

        return CustomReturnables.getOkResponseMap("Rola użytkownika gry została zaktualizowana.");

    }
}

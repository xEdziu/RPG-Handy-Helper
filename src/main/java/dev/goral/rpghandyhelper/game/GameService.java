package dev.goral.rpghandyhelper.game;

import dev.goral.rpghandyhelper.chat.GameRoomManager;
import dev.goral.rpghandyhelper.game.gameUsers.*;
import dev.goral.rpghandyhelper.rpgSystems.RpgSystemsRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ForbiddenActionException;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameUsersRepository gameUsersRepository;
    private final RpgSystemsRepository rpgSystemsRepository;
    private final GameRoomManager gameRoomManager;
    private final CpRedCharactersRepository cpRedCharactersRepository;

    public Map<String, Object> getAllGames() {
        List<Game> games = gameRepository.findAll();
        List<GameDTOAdmin> gamesDto = games.stream()
                .map(game -> new GameDTOAdmin(
                        game.getId(),
                        game.getName(),
                        game.getDescription(),
                        game.getOwner().getId(),
                        game.getRpgSystem().getId(),
                        game.getStatus().toString()
                ))
                .toList();

        if (gamesDto.isEmpty()) {
            throw new ResourceNotFoundException("Nie znaleziono żadnych gier.");
        }
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wszystkie gry.");
        response.put("games", gamesDto);
        return response;
    }

    public Map<String, Object> getGame(Long gameId) {
        GameDTO gameResponse = gameRepository.findGameById(gameId)
                .map(game -> new GameDTO(
                        game.getId(),
                        game.getName(),
                        game.getDescription(),
                        game.getOwner().getId(),
                        game.getRpgSystem().getId()
                ))
                .orElse(null);

        if (gameResponse == null) {
            throw new ResourceNotFoundException("Gra o podanym ID nie istnieje.");
        }
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano grę.");
        response.put("game", gameResponse);
        return response;
    }

    public Map<String, Object> getGamePlayers(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o podanym ID nie istnieje."));

        List<GameUsers> gameUsers = gameUsersRepository.findGameAllUsersByGameId(gameId);
        List<GameUsersDTO> gameUsersDTO = gameUsers.stream()
                .map(gameUser -> new GameUsersDTO(
                        gameUser.getId(),
                        gameUser.getUser().getId(),
                        gameUser.getGame().getId(),
                        gameUser.getRole().toString()
                ))
                .toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano listę użytkowników gry.");
        response.put("gameUsers", gameUsersDTO);
        return response;
    }

    public Map<String, Object> getUserGames(User currentUser) {
        List<GameUsers> gameUsers = gameUsersRepository.findAllByUserId(currentUser.getId());
        List<UserGamesDTO> userGamesDTO = gameUsers.stream()
                .filter(gameUser -> gameUser.getGame().getStatus() == GameStatus.ACTIVE)
                .map(gameUser -> new UserGamesDTO(
                        gameUser.getGame().getId(),
                        gameUser.getGame().getRpgSystem().getId(),
                        gameUser.getGame().getRpgSystem().getName(),
                        gameUser.getGame().getName(),
                        gameUser.getGame().getDescription()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano listę gier użytkownika.");
        response.put("userGames", userGamesDTO);
        return response;
    }

    public Map<String, Object> getUserGamesWithPlayers(User currentUser) {
        List<GameUsers> gameUsers = gameUsersRepository.findAllByUserId(currentUser.getId());
        List<FullGameInfoDTO> fullGameInfoDTOS = new ArrayList<>();
        for (GameUsers gameUser : gameUsers) {
            Game game = gameUser.getGame();
            if (game.getStatus() == GameStatus.ACTIVE) {
                List<GameUsers> players = gameUsersRepository.findGameAllUsersByGameId(game.getId());
                List<SimpleUserInGameDTO> playerDTOs = players.stream()
                        .map(player -> new SimpleUserInGameDTO(
                                player.getUser().getId(),
                                player.getUser().getUsername(),
                                player.getUser().getFirstName(),
                                player.getRole(),
                                player.getUser().getUserPhotoPath()
                        )).toList();

                FullGameInfoDTO fullGameInfoDTO = new FullGameInfoDTO(
                        game.getId(),
                        game.getName(),
                        game.getDescription(),
                        game.getRpgSystem().getName(),
                        playerDTOs
                );
                fullGameInfoDTOS.add(fullGameInfoDTO);
            }
        }
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano listę gier użytkownika z graczami.");
        response.put("userGames", fullGameInfoDTOS);
        return response;
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
        game.setOwner(currentUser);
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

        // Sprawdź, czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalArgumentException("Nie można dodać użytkownika do nieaktywnej gry.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdzenie, czy na pewno to GM próbuje zmienić swoja gre
        GameUsers gameUser = gameUsersRepository.findByGameIdAndUserId(request.getGameId(), currentUser.getId());
        if (gameUser == null || gameUser.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalArgumentException("Nie możesz dodać użytkownika do gry, gdy nie jesteś jej GameMasterem.");
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

        CpRedCharacters character = cpRedCharactersRepository.findByUserId_IdAndGameId_Id(request.getUserId(), request.getGameId());

        if(character!=null) {
            character.setUser(null);
            character.setType(CpRedCharactersType.NPC);
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
        GameUsers gameUser = gameUsersRepository.findByGameIdAndUserId(gameId, currentUser.getId());
        if (gameUser == null || gameUser.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalArgumentException("Nie możesz zmienić gry gdy nie jesteś jej GameMasterem.");
        }

        // Sprawdź, czy gra jest aktywna
        if (gameToUpdate.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalArgumentException("Nie można edytować nieaktywnej gry.");
        }

        if(game.getName() == null && game.getOwner() == null && game.getDescription() == null) {
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

        if (game.getOwner() != null ) {

            if(gameToUpdate.getOwner().getId() == null) {
                throw new IllegalArgumentException("Gra musi mieć GameMastera.");
            }
            if(!userRepository.existsById(game.getOwner().getId())){
                throw new ResourceNotFoundException("GameMaster o id " + game.getOwner().getId() +" nie istnieje.");
            }

            if(!gameUsersRepository.existsByUserIdAndGameId(game.getOwner().getId(), gameId)){
                AddUserToGameRequest addUserToGameRequest = new AddUserToGameRequest();
                addUserToGameRequest.setUserId(game.getOwner().getId());
                addUserToGameRequest.setGameId(gameId);
                addUserToGameRequest.setRole(GameUsersRole.GAMEMASTER.toString());
                addUserToGame(addUserToGameRequest);
            }
            Map<String, String> roleUpdateGameMaster = Map.of("role", "GAMEMASTER");
            Map<String, String> roleUpdatePlayer = Map.of("role", "PLAYER");

            updateGameUserRole(gameUsersRepository.findIdByUserIdAndGameId(game.getOwner().getId(), gameId), roleUpdateGameMaster);
            updateGameUserRole(gameUsersRepository.findIdByUserIdAndGameId(gameToUpdate.getOwner().getId(), gameId), roleUpdatePlayer);

            gameToUpdate.setOwner(game.getOwner());
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
        GameUsers gameUser = gameUsersRepository.findByGameIdAndUserId(gameId, currentUser.getId());
        if (gameUser == null || gameUser.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalArgumentException("Nie możesz zmienić statusu gry, gdy nie jesteś jej GameMasterem.");
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Gracz od zmiany
        GameUsers gameUser = gameUsersRepository.findById(gameUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Użytkownik gry o podanym ID nie istnieje."));

        // Gra, do której przypisany jest gracz
        Game game = gameRepository.findById(gameUser.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o podanym ID nie istnieje."));

        // Sprawdzenie, czy na pewno to GM próbuje zmienić swoja gre
        GameUsers gameUserGM = gameUsersRepository.findByGameIdAndUserId(game.getId(), currentUser.getId());

        if (gameUserGM == null || gameUserGM.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalArgumentException("Nie możesz zmienić roli użytkownika, gdy nie jesteś jej GameMasterem.");
        }

        // Sprawdzenie, czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalArgumentException("Nie można zmienić roli w nieaktywnej grze.");
        }

        if (request == null || request.isEmpty()) {
            throw new IllegalArgumentException("Brak danych do aktualizacji.");
        }

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
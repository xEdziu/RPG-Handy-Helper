package dev.goral.rpghandyhelper.chat;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ForbiddenActionException;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/authorized/gameRoom")
public class GameRoomController {

    private final GameRoomManager gameRoomManager;
    private final GameRoomHistoryRepository historyRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;

    @PostMapping("/create")
    public Map<String, Object> createGameRoom(@RequestParam Long gameId,
                                              @AuthenticationPrincipal User currentUser) {
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Stworzono pokój gry.");

        Game game = gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            throw new ResourceNotFoundException("Nie znaleziono gry o podanym ID.");
        }

        if (game.getStatus() != GameStatus.ACTIVE){
            throw new ForbiddenActionException("Nie można stworzyć pokoju dla nieaktywnej gry.");
        }

        GameUsers gameUser = gameUsersRepository.findByGameIdAndUserId(gameId, currentUser.getId());
        if (gameUser == null || gameUser.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalArgumentException("Tylko GameMaster może stworzyć pokój dla tej gry.");
        }

        GameRoom room = gameRoomManager.createRoom(gameId, currentUser.getId());

        response.put("roomId", room.getRoomId());
        response.put("url", "/room/" + room.getRoomId());

        return  response;
    }

    @GetMapping("/gameIdForRoomId/{roomId}")
    public Map<String, Object> getGameIdForRoomId(@PathVariable String roomId) {
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Znaleziono ID gry dla podanego pokoju.");

        GameRoom room = gameRoomManager.getRoomById(roomId);
        if (room == null) {
            throw new ResourceNotFoundException("Nie znaleziono pokoju o podanym ID.");
        }

        response.put("gameId", room.getGameId());
        return response;
    }

    @GetMapping("/history")
    public Map<String, Object> getUserGameRoomHistory(@AuthenticationPrincipal User currentUser) {
        List<GameRoomHistory> userHistory = historyRepository.findByParticipantsContaining(currentUser);

        List<Map<String, Object>> historyReturnable = userHistory.stream().map(history -> {
            Map<String, Object> map = new HashMap<>();
            map.put("roomId", history.getRoomId());
            map.put("gameId", history.getGame().getId());
            map.put("gameName", history.getGame().getName());
            map.put("startedAt", history.getStartedAt());
            map.put("endedAt", history.getEndedAt());
            map.put("durationMinutes", Optional.ofNullable(history.getEndedAt())
                    .map(end -> (end.getTime() - history.getStartedAt().getTime()) / 60000)
                    .orElse(null));
            map.put("createdBy", history.getCreator().getUsername());
            map.put("participants", history.getParticipants()
                    .stream()
                    .map(User::getUsername)
                    .collect(Collectors.toSet()));
            return map;
        }).toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Historia pokoi gry.");
        response.put("history", historyReturnable);
        return response;
    }

    @GetMapping("/active")
    public Map<String, Object> getActiveRoomsForGame(@RequestParam Long gameId,
                                                     @AuthenticationPrincipal User currentUser) {
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Znaleziono aktywne pokoje.");

        // Check if the game exists
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            throw new ResourceNotFoundException("Nie znaleziono gry o podanym ID.");
        }

        // Check if the user is a participant of the game
        Optional<GameUsers> gameUsers = gameUsersRepository.findUserInGame(gameId, currentUser.getId());
        if (gameUsers.isEmpty()) {
            throw new ForbiddenActionException("Nie jesteś uczestnikiem tej gry.");
        }

        List<Map<String, Object>> activeRooms = gameRoomManager.getActiveRoomsForGame(gameId);

        if (activeRooms.isEmpty()) {
            return CustomReturnables.getOkResponseMap("Nie znaleziono aktywnych pokoi dla tej gry.");
        }

        response.put("activeRooms", activeRooms);
        return response;
    }

}
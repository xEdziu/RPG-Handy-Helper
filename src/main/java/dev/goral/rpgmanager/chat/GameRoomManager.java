package dev.goral.rpgmanager.chat;

import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GameRoomManager {

    private final Map<String, GameRoom> activeRooms = new ConcurrentHashMap<>();

    private final GameRoomHistoryRepository historyRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public GameRoom createRoom(Long gameId, Long creatorId) {
        String roomId = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        GameRoom room = new GameRoom(roomId, gameId, creatorId);
        activeRooms.put(roomId, room);
        return room;
    }

    public void addUser(String roomId, String username, String sessionId) {
        GameRoom room = activeRooms.get(roomId);
        if (room != null) {
            room.addUser(username, sessionId);
        }
    }

    public void removeUserBySession(String sessionId) {
        List<String> emptyRooms = new ArrayList<>();

        for (GameRoom room : activeRooms.values()) {
            boolean hadSession = room.getConnectedUsersOrdered().stream()
                    .anyMatch(username -> sessionId.equals(room.getConnectedSessionId(username)));

            room.removeUserBySession(sessionId);

            if (hadSession) {
                List<String> updatedUsers = room.getConnectedUsersOrdered();
                messagingTemplate.convertAndSend("/topic/chat/" + room.getRoomId() + "/users", updatedUsers);
            }

            if (room.isEmpty()) {
                saveRoomHistory(room);
                emptyRooms.add(room.getRoomId());
            }
        }

        emptyRooms.forEach(activeRooms::remove);
    }

    public List<String> getConnectedUsers(String roomId) {
        GameRoom room = activeRooms.get(roomId);
        return room != null ? room.getConnectedUsersOrdered() : List.of();
    }

    private void saveRoomHistory(GameRoom room) {
        GameRoomHistory history = new GameRoomHistory();
        history.setRoomId(room.getRoomId());
        history.setStartedAt(Timestamp.from(room.getCreatedAt()));
        history.setEndedAt(Timestamp.from(Instant.now()));
        history.setGame(gameRepository.findById(room.getGameId()).orElseThrow());
        history.setCreator(userRepository.findById(room.getCreatorId()).orElseThrow());

        Set<User> users = room.getAllParticipants().stream()
                .map(userRepository::findByUsername)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        history.setParticipants(users);
        historyRepository.save(history);
    }

    public List<Map<String, Object>> getActiveRoomsForGame(Long gameId) {
        List<Map<String, Object>> activeRoomsForGame = new ArrayList<>();

        for (GameRoom room : activeRooms.values()) {
            if (room.getGameId().equals(gameId)) {
                Map<String, Object> roomInfo = new HashMap<>();
                roomInfo.put("roomId", room.getRoomId());
                roomInfo.put("gameId", room.getGameId());
                roomInfo.put("creatorId", room.getCreatorId());
                roomInfo.put("createdAt", room.getCreatedAt());
                roomInfo.put("connectedUsers", room.getConnectedUsersOrdered());
                activeRoomsForGame.add(roomInfo);
            }
        }

        return activeRoomsForGame;
    }

    public GameRoom getRoom(String roomId) {
        return activeRooms.get(roomId);
    }
}

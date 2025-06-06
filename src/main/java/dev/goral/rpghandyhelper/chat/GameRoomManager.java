package dev.goral.rpghandyhelper.chat;

import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class GameRoomManager {


    private final Map<String, GameRoom> activeRooms = new ConcurrentHashMap<>();

    private final GameRoomHistoryRepository historyRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public GameRoom createRoom(Long gameId, Long creatorId) {
        String roomId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        // Check if the room ID is already in use
        while (activeRooms.containsKey(roomId)) {
            roomId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
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

    public synchronized void removeUserBySession(String sessionId) {
        GameRoom room = activeRooms.values().stream()
                .filter(r -> r.getUserSessions().containsValue(sessionId))
                .findFirst()
                .orElse(null);

        if (room != null) {
            room.removeUserBySession(sessionId);

            if (room.isEmpty()) {
                String roomId = room.getRoomId();

                // Opóźnione usunięcie pokoju po 60 sekundach
                scheduler.schedule(() -> {
                    GameRoom stillEmpty = activeRooms.get(roomId);
                    if (stillEmpty != null && stillEmpty.isEmpty()) {
                        saveRoomHistory(stillEmpty);
                        activeRooms.remove(roomId);
                        System.out.println("Pokój " + roomId + " został usunięty po 30 sekundach bez użytkowników.");
                    }
                }, 30, TimeUnit.SECONDS);
            }
        }
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

    public GameRoom getRoomById(String roomId) {
        return activeRooms.get(roomId);
    }
}

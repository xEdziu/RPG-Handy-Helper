package dev.goral.rpgmanager.chat;

import lombok.Getter;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class GameRoom {
    private final String roomId;
    private final Long gameId;
    private final Long creatorId;
    private final Instant createdAt = Instant.now();

    private final Map<String, String> userSessions = new ConcurrentHashMap<>();
    private final List<String> joinOrder = Collections.synchronizedList(new ArrayList<>());
    private final Set<String> allParticipants = ConcurrentHashMap.newKeySet();

    public GameRoom(String roomId, Long gameId, Long creatorId) {
        this.roomId = roomId;
        this.gameId = gameId;
        this.creatorId = creatorId;
    }

    public void addUser(String username, String sessionId) {
        userSessions.put(username, sessionId);
        System.out.println("User " + username + " joined room " + roomId);
        if (!joinOrder.contains(username)) {
            joinOrder.add(username);
        }
        allParticipants.add(username);
    }

    public void removeUserBySession(String sessionId) {
        userSessions.values().removeIf(s -> s.equals(sessionId));
    }

    public List<String> getConnectedUsersOrdered() {
        return joinOrder.stream()
                .filter(userSessions::containsKey)
                .toList();
    }

    public String getConnectedSessionId(String username) {
        return userSessions.get(username);
    }


    public boolean isEmpty() {
        return userSessions.isEmpty();
    }
}

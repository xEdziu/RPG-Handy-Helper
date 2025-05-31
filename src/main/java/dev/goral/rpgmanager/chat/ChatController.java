package dev.goral.rpgmanager.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.context.event.EventListener;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final GameRoomManager roomManager;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage send(@DestinationVariable String roomId,
                            ChatMessage message,
                            Principal principal,
                            StompHeaderAccessor accessor) {

        String sessionId = accessor.getSessionId();
        roomManager.addUser(roomId, principal.getName(), sessionId);

        List<String> users = roomManager.getConnectedUsers(roomId);
        System.out.println("Connected users in room " + roomId + ": " + users);
        messagingTemplate.convertAndSend("/topic/chat/" + roomId + "/users", users);

        return message;
    }

    @MessageMapping("/join/{roomId}")
    public void join(@DestinationVariable String roomId,
                     Principal principal,
                     StompHeaderAccessor accessor) {

        String sessionId = accessor.getSessionId();
        roomManager.addUser(roomId, principal.getName(), sessionId);

        List<String> users = roomManager.getConnectedUsers(roomId);
        messagingTemplate.convertAndSend("/topic/chat/" + roomId + "/users", users);
    }

    @MessageMapping("/dice/{roomId}")
    public void rollDice(@DestinationVariable String roomId, @Payload ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/dice/" + roomId, message);
    }


    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        System.out.println("User disconnected: " + event.getSessionId());
        roomManager.removeUserBySession(event.getSessionId());
    }
}

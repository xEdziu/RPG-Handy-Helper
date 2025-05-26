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
    public void send(@DestinationVariable String roomId,
                     @Payload ChatMessage message,
                     Principal principal,
                     StompHeaderAccessor accessor) {

        String sender = principal.getName();
        String text = message.getContent().trim();

        // (re) dodajemy użytkownika do pokoju i odświeżamy listę
        roomManager.addUser(roomId, sender, accessor.getSessionId());
        List<String> users = roomManager.getConnectedUsers(roomId);
        messagingTemplate.convertAndSend("/topic/chat/" + roomId + "/users", users);

        if (text.startsWith("/pv ")) {
            String[] parts = text.split("\\s+", 3);
            if (parts.length < 3) {
                // niepoprawna składnia
                messagingTemplate.convertAndSendToUser(
                        sender,
                        "/topic/chat/" + roomId,
                        new ChatMessage("System", "Użyj: /pv <nick> <wiadomość>", true)
                );
                return;
            }
            String target = parts[1];
            String privateText = parts[2];

            if (!users.contains(target)) {
                // adresat nie w pokoju
                messagingTemplate.convertAndSendToUser(
                        sender,
                        "/topic/chat/" + roomId,
                        new ChatMessage("System", "Użytkownik " + target + " nie jest w pokoju", true)
                );
                return;
            }
            if (target.equals(sender)) {
                // wiadomość do siebie
                messagingTemplate.convertAndSendToUser(
                        sender,
                        "/topic/chat/" + roomId,
                        new ChatMessage("System", "Nie możesz wysłać wiadomości prywatnej do siebie", true)
                );
                return;
            }
            // wysyłamy prywatnie
            ChatMessage pmTo = new ChatMessage();
            pmTo.setFrom(sender);
            pmTo.setContent(privateText);
            pmTo.setPrivateMessage(true);
            messagingTemplate.convertAndSendToUser(
                    target,
                    "/topic/chat/" + roomId,
                    pmTo
            );
            // potwierdzenie u nadawcy
            ChatMessage pmFrom = new ChatMessage();
            pmFrom.setFrom("Do " + target);
            pmFrom.setContent(privateText);
            pmFrom.setPrivateMessage(true);
            messagingTemplate.convertAndSendToUser(
                    sender,
                    "/topic/chat/" + roomId,
                    pmFrom
            );

        } else {
            // zwykły broadcast
            ChatMessage broadcast = new ChatMessage();
            broadcast.setFrom(sender);
            broadcast.setContent(text);
            broadcast.setPrivateMessage(false);
            messagingTemplate.convertAndSend("/topic/chat/" + roomId, broadcast);
        }
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

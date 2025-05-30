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
                     @Payload ChatMessage incomingMessage,
                     Principal principal,
                     StompHeaderAccessor accessor) {

        String messageContent = incomingMessage.getContent().trim();
        String clientReportedSender = incomingMessage.getFrom(); // Kto wg klienta wysłał wiadomość
        String authenticatedSender = principal.getName(); // Kto faktycznie jest zalogowany

        // Zarządzanie użytkownikami w pokoju: dodajemy/odświeżamy tylko, gdy wiadomość jest od rzeczywistego użytkownika
        if (!"System".equalsIgnoreCase(clientReportedSender)) {
            roomManager.addUser(roomId, authenticatedSender, accessor.getSessionId());
            List<String> usersInRoom = roomManager.getConnectedUsers(roomId);
            messagingTemplate.convertAndSend("/topic/chat/" + roomId + "/users", usersInRoom);
        }

        if (messageContent.startsWith("/pv ") && !"System".equalsIgnoreCase(clientReportedSender)) {
            // Logika dla wiadomości prywatnych
            String[] parts = messageContent.split("\\s+", 3);
            if (parts.length < 3) {
                messagingTemplate.convertAndSendToUser(
                        authenticatedSender,
                        "/topic/chat/" + roomId,
                        new ChatMessage("System", "Użyj: /pv <nick> <wiadomość>", true)
                );
                return;
            }
            String targetUser = parts[1];
            String privateText = parts[2];
            List<String> currentUsersInRoom = roomManager.getConnectedUsers(roomId);

            if (!currentUsersInRoom.contains(targetUser)) {
                messagingTemplate.convertAndSendToUser(
                        authenticatedSender,
                        "/topic/chat/" + roomId,
                        new ChatMessage("System", "Użytkownik " + targetUser + " nie jest w pokoju.", true)
                );
                return;
            }
            if (targetUser.equals(authenticatedSender)) {
                messagingTemplate.convertAndSendToUser(
                        authenticatedSender,
                        "/topic/chat/" + roomId,
                        new ChatMessage("System", "Nie możesz wysłać wiadomości prywatnej do siebie.", true)
                );
                return;
            }

            ChatMessage privateMessageToTarget = new ChatMessage(authenticatedSender, privateText, true);
            messagingTemplate.convertAndSendToUser(targetUser, "/topic/chat/" + roomId, privateMessageToTarget);

            ChatMessage confirmationToSender = new ChatMessage("Do " + targetUser, privateText, true);
            messagingTemplate.convertAndSendToUser(authenticatedSender, "/topic/chat/" + roomId, confirmationToSender);

        } else {
            ChatMessage broadcastMessage = new ChatMessage();
            broadcastMessage.setContent(messageContent);
            broadcastMessage.setPrivateMessage(false);

            // Wykrywamy rzuty kością (możesz rozszerzyć warunek)
            if (clientReportedSender != null && !clientReportedSender.equalsIgnoreCase("System")) {
                // Sprawdzamy, czy to rzuty kością-np. czy content zawiera "wyrzucił" lub "wylosował"
                if (messageContent.toLowerCase().contains("wyrzucił") || messageContent.toLowerCase().contains("wylosował")) {
                    broadcastMessage.setFrom("🎲 Rzut kością");
                } else {
                    broadcastMessage.setFrom(authenticatedSender);
                }
            } else {
                broadcastMessage.setFrom("System");
            }

            messagingTemplate.convertAndSend("/topic/chat/" + roomId, broadcastMessage);
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

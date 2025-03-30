package dev.goral.rpgmanager.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class StaticRoomRedirectController {

    private final GameRoomManager roomManager;

    @GetMapping("/room/{roomId}")
    public String forwardRoomPage(@PathVariable String roomId) {
        GameRoom room = roomManager.getRoom(roomId);

        if (room == null) {
            // Wyślij 404, jeśli pokój nie istnieje
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Pokój nie istnieje lub już się zakończył"
            );
        } else {
            return "forward:/room.html";
        }
    }
}

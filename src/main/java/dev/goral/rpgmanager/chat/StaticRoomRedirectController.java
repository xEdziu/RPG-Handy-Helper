package dev.goral.rpgmanager.chat;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class StaticRoomRedirectController {

    private final GameRoomManager roomManager;

    @GetMapping("/room/{roomId}")
    public void forwardRoomPage(@PathVariable String roomId, HttpServletResponse response) throws IOException {
        GameRoom room = roomManager.getRoom(roomId);

        if (room == null) {
            // Można przekierować na stronę błędu albo zwrócić 404
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Pokój nie istnieje lub już się zakończył.");
        } else {
            response.sendRedirect("/room.html"); // Pokój istnieje – idź do widoku
        }
    }
}

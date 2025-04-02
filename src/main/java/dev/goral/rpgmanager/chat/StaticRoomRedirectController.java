package dev.goral.rpgmanager.chat;

import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.user.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class StaticRoomRedirectController {

    private final GameRoomManager roomManager;
    private final GameUsersRepository gameUsersRepository;

    @GetMapping("/room/{roomId}")
    public String forwardRoomPage(@PathVariable String roomId,
                                  HttpServletResponse response,
                                  @AuthenticationPrincipal User currentUser) throws IOException {
        GameRoom room = roomManager.getRoom(roomId);

        if (room == null) {
            response.sendRedirect("/error/roomNotFound.html");
            return null;
        }

        boolean isInGame = gameUsersRepository.existsByGameIdAndUserId(room.getGameId(), currentUser.getId());

        if (!isInGame) {
            response.sendRedirect("/error/unauthorizedRoom.html");
            return null;
        }

        return "forward:/room.html";
    }
}

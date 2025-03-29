package dev.goral.rpgmanager.chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StaticRoomRedirectController {

    @GetMapping("/room/{roomId}")
    public String serveRoomHtml(@PathVariable String roomId) {
        return "forward:/room.html"; // z /static/room.html
    }
}

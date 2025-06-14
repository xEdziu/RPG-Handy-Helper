package dev.goral.rpghandyhelper.mobile;

import dev.goral.rpghandyhelper.notes.GameNoteService;
import dev.goral.rpghandyhelper.user.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/mobile/v1/authorized")
public class MobileNotesController {

    private final GameNoteService gameNoteService;
    private final MobileAuthTools mobileAuthTools;

    @GetMapping(path = "/notes")
    public Map<String, Object> getAllUserNotes() {
        User loggedUser = mobileAuthTools.getUserFromJwt();
        return gameNoteService.getAllUsersNotes(loggedUser);
    }

    @GetMapping(path = "/notes/{gameId}")
    public Map<String, Object> getGameNotes(@PathVariable Long gameId) {
        User loggedUser = mobileAuthTools.getUserFromJwt();
        return gameNoteService.getGameNotesForGame(loggedUser, gameId);
    }

}

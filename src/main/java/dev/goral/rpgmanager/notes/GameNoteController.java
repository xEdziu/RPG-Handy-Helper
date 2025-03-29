package dev.goral.rpgmanager.notes;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/authorized/gameNotes")
@RequiredArgsConstructor
public class GameNoteController {

    private final GameNoteService gameNoteService;

    @PostMapping(path = "/add")
    public Map<String, Object> addGameNote(@RequestBody GameNoteDto gameNoteDto) {
        return gameNoteService.addGameNote(gameNoteDto);
    }

    @GetMapping
    public Map<String, Object> getGameNotes() {
        return gameNoteService.getGameNotes();
    }

    @GetMapping(path = "/{gameNoteId}")
    public Map<String, Object> getGameNoteById(@PathVariable Long gameNoteId) {
        return gameNoteService.getGameNotes(gameNoteId);
    }

    @GetMapping(path = "/game/{gameId}")
    public Map<String, Object> getGameNotesByGameId(@PathVariable Long gameId) {
        return gameNoteService.getGameNotesForGame(gameId);
    }

    @PutMapping(path = "/update/{gameNoteId}")
    public Map<String, Object> updateGameNote(@PathVariable Long gameNoteId, @RequestBody GameNoteDto gameNoteDto) {
        return gameNoteService.updateGameNote(gameNoteId, gameNoteDto);
    }

    @DeleteMapping(path = "/delete/{gameNoteId}")
    public Map<String, Object> deleteGameNoteById(@PathVariable Long gameNoteId) {
        return gameNoteService.deleteGameNote(gameNoteId);
    }
}

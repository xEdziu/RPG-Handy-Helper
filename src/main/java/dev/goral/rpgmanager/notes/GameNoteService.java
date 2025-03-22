package dev.goral.rpgmanager.notes;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.user.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameNoteService {

    private final GameNoteRepository gameNoteRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Map<String, Object> addGameNote(GameNoteDto gameNoteDto) {
        Object principal = getAuthentication().getPrincipal();

        if (!(principal instanceof User loggedInUser)) {
            throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");
        }

        if (gameNoteDto.getUserId() == null) {
            throw new IllegalStateException("Nota musi być przypisana do użytkownika.");
        }

        if (!loggedInUser.getId().equals(gameNoteDto.getUserId())) {
            throw new IllegalStateException("Nie masz uprawnień do dodawania notatek dla innych użytkowników.");
        }

        if (gameNoteDto.getGameId() == null) {
            throw new IllegalStateException("Notatka musi być przypisana do gry.");
        }

        if (gameNoteDto.getContent() == null || gameNoteDto.getContent().isEmpty()) {
            throw new IllegalStateException("Notatka nie może być pusta.");
        }

        if (!gameRepository.existsById(gameNoteDto.getGameId())) {
            throw new IllegalStateException("Gra o podanym ID nie istnieje.");
        }

        if (!userRepository.existsById(gameNoteDto.getUserId())) {
            throw new IllegalStateException("Użytkownik o podanym ID nie istnieje.");
        }

        GameNote gameNote = new GameNote();
        Optional<Game> game = gameRepository.findGameById(gameNoteDto.getGameId());
        Optional<User> user = userRepository.findById(gameNote.getId());
        gameNote.setGame(game.get());
        gameNote.setUser(user.get());
        gameNote.setContent(gameNoteDto.getContent());

        gameNoteRepository.save(gameNote);
        return CustomReturnables.getOkResponseMap("Notatka została dodana");
    }
}

package dev.goral.rpgmanager.notes;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameStatus;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.game.gameUsers.GameUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.user.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GameNoteService {

    private final GameNoteRepository gameNoteRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final UserRepository userRepository;

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Map<String, Object> addGameNote(GameNoteDto gameNoteDto) {

        System.out.println("GameNoteService.addGameNote");
        System.out.println("gameNoteDto: " + gameNoteDto);

        Object principal = getAuthentication().getPrincipal();

        Game game = gameRepository.findById(gameNoteDto.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + gameNoteDto.getGameId() + " nie istnieje."));

        Optional<User> user = userRepository.findById(gameNoteDto.getUserId());

        if (!(principal instanceof User loggedInUser))
            throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");

        if (gameNoteDto.getUserId() == null)
            throw new IllegalStateException("Notatka musi być przypisana do użytkownika.");

        if (!loggedInUser.getId().equals(gameNoteDto.getUserId()))
            throw new IllegalStateException("Nie masz uprawnień do dodawania notatek dla innych użytkowników.");

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + gameNoteDto.getGameId() + " nie jest aktywna.");
        }

        if (gameNoteDto.getGameId() == null)
            throw new IllegalStateException("Notatka musi być przypisana do gry.");

        if (gameNoteDto.getTitle() == null || gameNoteDto.getTitle().isEmpty())
            throw new IllegalStateException("Notatka musi posiadać tytuł.");

        if (gameNoteDto.getContent() == null || gameNoteDto.getContent().isEmpty())
            throw new IllegalStateException("Notatka nie może być pusta.");

        if (!userRepository.existsById(gameNoteDto.getUserId()))
            throw new IllegalStateException("Użytkownik o podanym ID nie istnieje.");

        if (user.isEmpty())
            throw new IllegalStateException("Użytkownik o podanym ID nie istnieje.");

        // Check if user has already added a note for this game with the same title
        Optional<GameNote> gameNoteTest = gameNoteRepository.findByUserIdAndTitleIgnoreCaseAndGameId((gameNoteDto.getUserId()), gameNoteDto.getTitle(), gameNoteDto.getGameId());
        if (gameNoteTest.isPresent())
            throw new IllegalStateException("Posiadasz już notatkę o takim tytule.");

        GameNote gameNote = new GameNote();
        gameNote.setGame(game);
        gameNote.setUser(user.get());
        gameNote.setTitle(gameNoteDto.getTitle());
        gameNote.setContent(gameNoteDto.getContent());

        gameNoteRepository.save(gameNote);
        return CustomReturnables.getOkResponseMap("Notatka została dodana");
    }

    public Map<String, Object> getGameNotes() {
        Object principal = getAuthentication().getPrincipal();

        if (!(principal instanceof User loggedInUser))
            throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");

        List<GameNote> gameNotes = gameNoteRepository.findByUserId(loggedInUser.getId());
        List<GameNoteWithGameIdDto> gameNoteDtos = new ArrayList<>();

        for (GameNote gameNote : gameNotes) {
            GameNoteWithGameIdDto gameNoteDto = new GameNoteWithGameIdDto();
            gameNoteDto.setId(gameNote.getId());
            gameNoteDto.setTitle(gameNote.getTitle());
            gameNoteDto.setContent(gameNote.getContent());
            gameNoteDto.setCreatedAt(gameNote.getCreatedAt());
            gameNoteDto.setUpdatedAt(gameNote.getUpdatedAt());
            gameNoteDto.setGameId(gameNote.getGame().getId());
            gameNoteDto.setGameName(gameNote.getGame().getName());
            gameNoteDto.setRpgSystemName(gameNote.getGame().getRpgSystem().getName());
            gameNoteDtos.add(gameNoteDto);
        }

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Notatki zostały pobrane");
        response.put("gameNotes", gameNoteDtos);

        return response;
    }

    public Map<String, Object> getGameNotes(Long id) {
        Object principal = getAuthentication().getPrincipal();

        if (!(principal instanceof User))
            throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");

        Optional<GameNote> gameNotes = gameNoteRepository.findById(id);

        if (gameNotes.isEmpty())
            throw new IllegalStateException("Notatka o podanym ID nie istnieje.");

        if (!gameNotes.get().getUser().getId().equals(((User) principal).getId()))
            throw new IllegalStateException("Nie masz uprawnień do pobierania notatek innych użytkowników.");

        //get game name and rpg system name
        if (!gameRepository.existsById(gameNotes.get().getGame().getId()))
            throw new IllegalStateException("Gra o podanym ID nie istnieje.");

        Game game = gameRepository.findGameById(gameNotes.get().getGame().getId()).orElseThrow(() -> new IllegalStateException("Gra o podanym ID nie istnieje."));

        GameNoteViewDto gameNote = new GameNoteViewDto();
        gameNote.setId(gameNotes.get().getId());
        gameNote.setTitle(gameNotes.get().getTitle());
        gameNote.setContent(gameNotes.get().getContent());
        gameNote.setCreatedAt(gameNotes.get().getCreatedAt());
        gameNote.setUpdatedAt(gameNotes.get().getUpdatedAt());
        gameNote.setGameName(game.getName());
        gameNote.setRpgSystemName(game.getRpgSystem().getName());

        Map<String, Object> result = CustomReturnables.getOkResponseMap("Notatka została pobrana");
        result.put("gameNote", gameNote);

        return result;
    }

    public Map<String, Object> getGameNotesForGame(Long gameId) {
        Object principal = getAuthentication().getPrincipal();

        if (!(principal instanceof User loggedInUser))
            throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");

        if (!gameRepository.existsById(gameId))
            throw new IllegalStateException("Gra o podanym ID nie istnieje.");

        // check if user has access to the game
        boolean hasAccess = false;
        for (GameUsers gameUser : gameUsersRepository.findGameAllUsersByGameId(gameId)) {
            if (gameUser.getUser().getId().equals(loggedInUser.getId())) {
                hasAccess = true;
                break;
            }
        }
        if (!hasAccess) {
            throw new IllegalStateException("Nie masz uprawnień do pobierania notatek dla tej gry.");
        }

        List<GameNote> gameNotes = gameNoteRepository.findByGameIdAndUserId(gameId, loggedInUser.getId());

        List<GameNoteViewDto> gameNoteDtos = new ArrayList<>();
        for (GameNote gameNote : gameNotes) {
            GameNoteViewDto gameNoteDto = new GameNoteViewDto();
            gameNoteDto.setId(gameNote.getId());
            gameNoteDto.setTitle(gameNote.getTitle());
            gameNoteDto.setContent(gameNote.getContent());
            gameNoteDto.setCreatedAt(gameNote.getCreatedAt());
            gameNoteDto.setUpdatedAt(gameNote.getUpdatedAt());
            gameNoteDto.setGameName(gameNote.getGame().getName());
            gameNoteDto.setRpgSystemName(gameNote.getGame().getRpgSystem().getName());
            gameNoteDtos.add(gameNoteDto);
        }

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Notatki zostały pobrane");
        response.put("gameNotes", gameNoteDtos);

        return response;
    }

    public Map<String, Object> deleteGameNote(Long id) {
        Object principal = getAuthentication().getPrincipal();

        if (!(principal instanceof User loggedInUser))
            throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");

         Optional<GameNote> gameNote = gameNoteRepository.findById(id);

        if (gameNote.isEmpty())
            throw new IllegalStateException("Notatka o podanym ID nie istnieje.");

        if (!gameNote.get().getUser().getId().equals(loggedInUser.getId()))
            throw new IllegalStateException("Nie masz uprawnień do usuwania notatek innych użytkowników.");

        gameNoteRepository.deleteById(id);

        return CustomReturnables.getOkResponseMap("Notatka została usunięta");
    }

    public  Map<String, Object> updateGameNote(Long id, GameNoteDto gameNoteDto) {
        Object principal = getAuthentication().getPrincipal();

        if (!(principal instanceof User loggedInUser))
            throw new IllegalStateException("Nie udało się pobrać zalogowanego użytkownika.");

        Game game = gameRepository.findById(gameNoteDto.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + gameNoteDto.getGameId() + " nie istnieje."));

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + gameNoteDto.getGameId() + " nie jest aktywna.");
        }

        Optional<GameNote> gameNote = gameNoteRepository.findById(id);

        if (gameNote.isEmpty())
            throw new IllegalStateException("Notatka o podanym ID nie istnieje.");

        if (!gameNote.get().getUser().getId().equals(loggedInUser.getId()))
            throw new IllegalStateException("Nie masz uprawnień do edytowania notatek innych użytkowników.");

        if (gameNoteDto.getTitle() == null || gameNoteDto.getTitle().isEmpty())
            throw new IllegalStateException("Notatka musi posiadać tytuł.");

        if (gameNoteDto.getContent() == null || gameNoteDto.getContent().isEmpty())
            throw new IllegalStateException("Notatka nie może być pusta.");

        if (!gameRepository.existsById(gameNoteDto.getGameId()))
            throw new IllegalStateException("Gra o podanym ID nie istnieje.");

        if (!userRepository.existsById(gameNoteDto.getUserId()))
            throw new IllegalStateException("Użytkownik o podanym ID nie istnieje.");

        User user = userRepository.findById(gameNoteDto.getUserId()).orElseThrow(() -> new IllegalStateException("Użytkownik o podanym ID nie istnieje."));

        gameNote.get().setTitle(gameNoteDto.getTitle());
        gameNote.get().setContent(gameNoteDto.getContent());
        gameNote.get().setGame(game);
        gameNote.get().setUser(user);

        gameNoteRepository.save(gameNote.get());

        return CustomReturnables.getOkResponseMap("Notatka została zaktualizowana");
    }
}

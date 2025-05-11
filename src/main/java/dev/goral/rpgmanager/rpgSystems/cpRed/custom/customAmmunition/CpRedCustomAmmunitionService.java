package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customAmmunition;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.game.GameStatus;
import dev.goral.rpgmanager.game.gameUsers.GameUsers;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRole;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomAmmunitionService {
    private final CpRedCustomAmmunitionRepository cpRedCustomAmmunitionRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;

    // Pobierz wszystkie amunicje
    public List<CpRedCustomAmmunitionDTO> getAllCustomAmmunition() {
        List<CpRedCustomAmmunition> allCustomAmmunitionList = cpRedCustomAmmunitionRepository.findAll();
        return allCustomAmmunitionList.stream()
                .map( cpRedCustomAmmunition -> new CpRedCustomAmmunitionDTO(
                        cpRedCustomAmmunition.getGameId().getId(),
                        cpRedCustomAmmunition.getName(),
                        cpRedCustomAmmunition.getDescription(),
                        cpRedCustomAmmunition.getPricePerBullet(),
                        cpRedCustomAmmunition.getAvailability().toString()
        )).toList();
    }

    // Pobierz amunicje po id
    public CpRedCustomAmmunitionDTO getCustomAmmunitionById(Long ammunitionId) {
        return cpRedCustomAmmunitionRepository.findById(ammunitionId)
                .map( cpRedCustomAmmunition -> new CpRedCustomAmmunitionDTO(
                        cpRedCustomAmmunition.getGameId().getId(),
                        cpRedCustomAmmunition.getName(),
                        cpRedCustomAmmunition.getDescription(),
                        cpRedCustomAmmunition.getPricePerBullet(),
                        cpRedCustomAmmunition.getAvailability().toString()
                )).orElseThrow(() -> new ResourceNotFoundException("Customowa amunicja o id " + ammunitionId + " nie istnieje") );
    }

    // Pobierz amunicje po grze
    public List<CpRedCustomAmmunitionDTO> getCustomAmmunitionByGameId(Long gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy gra o podanym id istnieje
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + gameId + " nie istnieje."));

        // Sprawdź, czy użytkownik należy do tej gry
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie jesteś graczem wybranej gry."));

        List<CpRedCustomAmmunition> gameCustomAmmunitionList = cpRedCustomAmmunitionRepository.findAllByGameId(game);
        return gameCustomAmmunitionList.stream()
                .map( cpRedCustomAmmunition -> new CpRedCustomAmmunitionDTO(
                        cpRedCustomAmmunition.getGameId().getId(),
                        cpRedCustomAmmunition.getName(),
                        cpRedCustomAmmunition.getDescription(),
                        cpRedCustomAmmunition.getPricePerBullet(),
                        cpRedCustomAmmunition.getAvailability().toString()
                )).toList();
    }

    // Dodaj amunicje
    public Map<String, Object> addCustomAmmunition(AddCustomAmmunitionRequest cpRedCustomAmmunition) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy wszystkie wymagane pola są wypełnione
        if (cpRedCustomAmmunition.getGameId() == null || cpRedCustomAmmunition.getName() == null ||
                cpRedCustomAmmunition.getDescription() == null || cpRedCustomAmmunition.getPricePerBullet() <= 0 ||
                cpRedCustomAmmunition.getAvailability() == null) {
            throw new IllegalStateException("Wszystkie pola muszą być wypełnione.");
        }

        // Sprawdź, czy podana gra istnieje
        Game game = gameRepository.findById(cpRedCustomAmmunition.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + cpRedCustomAmmunition.getGameId() + " nie istnieje."));

        // Sprawdź, czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + cpRedCustomAmmunition.getGameId() + " nie jest aktywna.");
        }

        // Sprawdź, czy użytkownik należy do tej gry
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), cpRedCustomAmmunition.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));

        // Sprawdź, czy użytkownik jest GM
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może dodać amunicję do gry.");
        }

        // Sprawdzenie, czy w danej grze jest już broń o tej nazwie
        if (cpRedCustomAmmunitionRepository.existsByNameAndGameId(cpRedCustomAmmunition.getName(), game)) {
            throw new IllegalStateException("Customowa amunicja o tej nazwie już istnieje w tej grze.");
        }
        // Sprawdza, czy nazwa umiejętności nie jest pusta lub składa się tylko z białych znaków
        if (cpRedCustomAmmunition.getName().isEmpty() || cpRedCustomAmmunition.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa amunicji nie może być pusta.");
        }
        // Sprawdź długość nazwy umiejętności
        if (cpRedCustomAmmunition.getName().length() > 255) {
            throw new IllegalStateException("Nazwa amunicji nie może być dłuższa niż 255 znaków.");
        }

        // Sprawdza, czy opis umiejętności nie jest pusty lub składa się tylko z białych znaków
        if (cpRedCustomAmmunition.getDescription().isEmpty() || cpRedCustomAmmunition.getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Opis amunicji nie może być pusty.");
        }
        // Sprawdź długość opisu umiejętności
        if (cpRedCustomAmmunition.getDescription().length() > 500) {
            throw new IllegalStateException("Opis amunicji nie może być dłuższy niż 500 znaków.");
        }

        // Sprawdź, czy cena nie jest mniejsza od 0
        if (cpRedCustomAmmunition.getPricePerBullet() < 0) {
            throw new IllegalStateException("Cena za pocisk nie może być mniejsza od 0.");
        }

        // Zapisanie amunicji do bazy danych
        CpRedCustomAmmunition newCustomAmmunition = new CpRedCustomAmmunition(
                0,
                game,
                cpRedCustomAmmunition.getName(),
                cpRedCustomAmmunition.getDescription(),
                cpRedCustomAmmunition.getPricePerBullet(),
                cpRedCustomAmmunition.getAvailability()
        );

        cpRedCustomAmmunitionRepository.save(newCustomAmmunition);
        return CustomReturnables.getOkResponseMap("Customowa amunicja została dodana.");
    }

    // Modyfikuj amunicje
    public Map<String, Object> updateCustomAmmunition(Long ammunitionId,
                                                      AddCustomAmmunitionRequest cpRedCustomAmmunition) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy amunicja o podanym id istnieje
        CpRedCustomAmmunition ammunitionToUpdate = cpRedCustomAmmunitionRepository.findById(ammunitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowa amunicja o id " + ammunitionId + " nie istnieje"));

        // Sprawdź, czy podana gra istnieje
        Game game = gameRepository.findById(ammunitionToUpdate.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + ammunitionToUpdate.getGameId().getId() + " nie istnieje."));

        // Sprawdź, czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + ammunitionToUpdate.getGameId().getId() + " nie jest aktywna.");
        }

        // Sprawdź, czy użytkownik należy do tej gry
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), ammunitionToUpdate.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));

        // Sprawdź, czy użytkownik jest GM
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może modyfikować amunicję.");
        }

        // Sprawdź nazwę
        if(cpRedCustomAmmunition.getName() != null) {
            // Sprawdzenie, czy amunicja o podanej nazwie już istnieje
            if (cpRedCustomAmmunitionRepository.existsByNameAndGameId(cpRedCustomAmmunition.getName(), ammunitionToUpdate.getGameId())) {
                throw new IllegalStateException("Customowa amunicja o tej nazwie już istnieje.");
            }
            // Sprawdzenie, czy nazwa nie jest pusta lub składa się tylko z białych znaków
            if (cpRedCustomAmmunition.getName().isEmpty() || cpRedCustomAmmunition.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa amunicji nie może być pusta.");
            }
            // Sprawdź długość nazwy
            if (cpRedCustomAmmunition.getName().length() > 255) {
                throw new IllegalStateException("Nazwa amunicji jest za długa. Maksymalna długość to 255 znaków.");
            }
            ammunitionToUpdate.setName(cpRedCustomAmmunition.getName());
        }

        // Sprawdzenie opisu
        if (cpRedCustomAmmunition.getDescription() != null) {
            // Sprawdź długość opisu
            if (cpRedCustomAmmunition.getDescription().length() > 500) {
                throw new IllegalStateException("Opis amunicji jest za długi. Maksymalna długość to 500 znaków.");
            }
            // Sprawdzenie, czy opis nie jest pusty lub składa się tylko z białych znaków
            if (cpRedCustomAmmunition.getDescription().isEmpty() || cpRedCustomAmmunition.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis amunicji nie może być pusty.");
            }
            ammunitionToUpdate.setDescription(cpRedCustomAmmunition.getDescription());
        }

        // Sprawdzenie ceny
        if (cpRedCustomAmmunition.getPricePerBullet() != ammunitionToUpdate.getPricePerBullet()) {
            if (cpRedCustomAmmunition.getPricePerBullet() != -1){
                if (cpRedCustomAmmunition.getPricePerBullet() <= 0) {
                    throw new IllegalStateException("Cena za pocisk musi być większa od 0.");
                }
                ammunitionToUpdate.setPricePerBullet(cpRedCustomAmmunition.getPricePerBullet());
            }
        }

        // Sprawdzenie dostępności
        if (cpRedCustomAmmunition.getAvailability() != null) {
            ammunitionToUpdate.setAvailability(cpRedCustomAmmunition.getAvailability());
        }

        // Zapisanie amunicji do bazy danych
        cpRedCustomAmmunitionRepository.save(ammunitionToUpdate);

        return CustomReturnables.getOkResponseMap("Customowa amunicja została zmodyfikowana.");
    }

    // Pobierz wszystkie amunicje dla admina
    public List<CpRedCustomAmmunition> getAllCustomAmmunitionForAdmin() {
        return cpRedCustomAmmunitionRepository.findAll();
    }
}

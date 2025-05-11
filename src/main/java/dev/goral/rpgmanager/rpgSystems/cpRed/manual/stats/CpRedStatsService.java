package dev.goral.rpgmanager.rpgSystems.cpRed.manual.stats;

import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import dev.goral.rpgmanager.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CpRedStatsService {
    private final CpRedStatsRepository cpRedStatsRepository;
    private final UserRepository userRepository;

    // Pobierz wszystkie statystyki
    public List<CpRedStatsDTO> getAllStats() {
        List<CpRedStats> cpRedStatsList = cpRedStatsRepository.findAll();
        return cpRedStatsList.stream()
                .map(cpRedStats -> new CpRedStatsDTO(
                        cpRedStats.getName(),
                        cpRedStats.getTag(),
                        cpRedStats.getDescription()
                )).collect(Collectors.toList());
    }

    // Pobierz statystyke po id
    public CpRedStatsDTO getStatById(Long id) {
        return cpRedStatsRepository.findById(id)
                .map(cpRedStats -> new CpRedStatsDTO(
                        cpRedStats.getName(),
                        cpRedStats.getTag(),
                        cpRedStats.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Statystyka o id " + id + " nie istnieje"));
    }

    // Pobierz wszystkie statystyki dla admina
    public List<CpRedStats> getAllStatsForAdmin() {
        return cpRedStatsRepository.findAll();
    }

    // Dodać statystyke
    public Map<String, Object> addStat(CpRedStats cpRedStats) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy użytkownik ma rolę ADMIN
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do dodawania statystyk.");
        }

        // Sprawdź, czy statystyka o tej samej nazwie już istnieje
        if (cpRedStatsRepository.existsByName(cpRedStats.getName())) {
            throw new IllegalStateException("Statystyka o tej samej nazwie już istnieje.");
        }

        if(cpRedStats.getName() == null || cpRedStats.getTag() == null || cpRedStats.getDescription() == null) {
            throw new IllegalStateException("Nie wszystkie pola zostały wypełnione.");
        }

        // Sprawdzenie, czy nazwa nie jest pusta lub skłąda się tylko z białych znaków
        if (cpRedStats.getName().isEmpty() || cpRedStats.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa statystyki nie może być pusta.");
        }
        // Sprawdź długość nazwy
        if (cpRedStats.getName().length() > 255) {
            throw new IllegalStateException("Nazwa statystyki jest za długa. Maksymalna długość to 255 znaków.");
        }

        // Sprawdzenie, czy taki tag już istnieje
        if (cpRedStatsRepository.existsByTag(cpRedStats.getTag())) {
            throw new IllegalStateException("Statystyka o tym tagu już istnieje.");
        }
        // Sprawdzenie, czy tag nie jest pusty lub skłąda się tylko z białych znaków
        if (cpRedStats.getTag().isEmpty() || cpRedStats.getTag().trim().isEmpty()) {
            throw new IllegalStateException("Tag statystyki nie może być pusty.");
        }
        // Strawdzenie długości tagu
        if (cpRedStats.getTag().length() > 255) {
            throw new IllegalStateException("Tag statystyki jest za długi. Maksymalna długość to 255 znaków.");
        }

        // Sprawdzenie, czy opis nie jest pusty lub skłąda się tylko z białych znaków
        if (cpRedStats.getDescription().isEmpty() || cpRedStats.getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Opis statystyki nie może być pusty.");
        }
        // Sprawdź długość opisu
        if (cpRedStats.getDescription().length() > 500) {
            throw new IllegalStateException("Opis statystyki jest za długi. Maksymalna długość to 500 znaków.");
        }

        // Zaspisanie statystyki do bazy danych
        cpRedStatsRepository.save(cpRedStats);
        return CustomReturnables.getOkResponseMap("Statystyka została dodana.");

    }

    // Modyfikować statystyke
    public Map<String, Object> updateStat(Long id, CpRedStats cpRedStats) {
        CpRedStats statToUpdate = cpRedStatsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Statystyka o id " + id + " nie istnieje"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy użytkownik ma rolę ADMIN
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do modyfikacji statystyk.");
        }

        // Sprawdzenie nazwy
        if (cpRedStats.getName() != null) {
            // Sprawdź, czy statystyka o tej samej nazwie już istnieje
            if (cpRedStatsRepository.existsByName(cpRedStats.getName())) {
                throw new IllegalStateException("Statystyka o tej samej nazwie już istnieje.");
            }
            // Sprawdź długość nazwy
            if (cpRedStats.getName().length() > 255) {
                throw new IllegalStateException("Nazwa statystyki jest za długa. Maksymalna długość to 255 znaków.");
            }
            // Sprawdzenie, czy nazwa nie jest pusta lub skłąda się tylko z białych znaków
            if (cpRedStats.getName().isEmpty() || cpRedStats.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa statystyki nie może być pusta.");
            }

            statToUpdate.setName(cpRedStats.getName());
        }

        // Sprawdzenie tagu
        if (cpRedStats.getTag() != null) {
            // Sprawdzenie, czy taki tag już istnieje
            if (cpRedStatsRepository.existsByTag(cpRedStats.getTag())) {
                throw new IllegalStateException("Statystyka o tym tagu już istnieje.");
            }
            // Sprawdź długość tagu
            if (cpRedStats.getTag().length() > 255) {
                throw new IllegalStateException("Tag statystyki jest za długi. Maksymalna długość to 255 znaków.");
            }
            // Sprawdzenie, czy tag nie jest pusty lub skłąda się tylko z białych znaków
            if (cpRedStats.getTag().isEmpty() || cpRedStats.getTag().trim().isEmpty()) {
                throw new IllegalStateException("Tag statystyki nie może być pusty.");
            }
            statToUpdate.setTag(cpRedStats.getTag());
        }

        // Sprawdzenie opisu
        if (cpRedStats.getDescription() != null) {
            // Sprawdź długość opisu
            if (cpRedStats.getDescription().length() > 500) {
                throw new IllegalStateException("Opis statystyki jest za długi. Maksymalna długość to 500 znaków.");
            }
            // Sprawdzenie, czy opis nie jest pusty lub skłąda się tylko z białych znaków
            if (cpRedStats.getDescription().isEmpty() || cpRedStats.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis statystyki nie może być pusty.");
            }
            statToUpdate.setDescription(cpRedStats.getDescription());
        }

        cpRedStatsRepository.save(statToUpdate);
        return CustomReturnables.getOkResponseMap("Statystyka została zaktualizowana.");
    }

    // Zmienić zmienność statystyki
    public Map<String, Object> changeStatChangeable(Long id) {
        CpRedStats statToUpdate = cpRedStatsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Statystyka o id " + id + " nie istnieje"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy użytkownik ma rolę ADMIN
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do modyfikacji statystyk.");
        }

        statToUpdate.setChangeable(!statToUpdate.isChangeable());
        cpRedStatsRepository.save(statToUpdate);
        return CustomReturnables.getOkResponseMap("Zmienność statystyki została zmieniona.");
    }
}

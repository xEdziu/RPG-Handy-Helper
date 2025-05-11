package dev.goral.rpgmanager.rpgSystems.cpRed.manual.skills;

import dev.goral.rpgmanager.rpgSystems.cpRed.manual.stats.CpRedStats;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.stats.CpRedStatsRepository;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.stats.CpRedStatsService;
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

@Service
@AllArgsConstructor
public class CpRedSkillsService {
    private final CpRedSkillsRepository cpRedSkillsRepository;
    private final UserRepository userRepository;
    private final CpRedStatsRepository cpRedStatsRepository;

    // Pobierz wszystkie umiejętności
    public List<CpRedSkillsDTO> getAllSkills(){
        List<CpRedSkills> cpRedSkillsList = cpRedSkillsRepository.findAll();
        return cpRedSkillsList.stream()
                .map(cpRedSkills -> new CpRedSkillsDTO(
                        cpRedSkills.getCategory().toString(),
                        cpRedSkills.getName(),
                        cpRedSkills.getConnectedStat().getTag(),
                        cpRedSkills.getDescription()
                )).toList();
    }

    // Pobierz umiejętność po id
    public CpRedSkillsDTO getSkillById(Long id) {
        return cpRedSkillsRepository.findById(id)
                .map(cpRedSkills -> new CpRedSkillsDTO(
                        cpRedSkills.getCategory().toString(),
                        cpRedSkills.getName(),
                        cpRedSkills.getConnectedStat().getTag(),
                        cpRedSkills.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Umiejętność o id " + id + " nie istnieje"));
    }

    // Pobierz wszystkie umiejętności dla admina
    public List<CpRedSkills> getAllSkillsForAdmin() {
        return cpRedSkillsRepository.findAll();
    }

    // Dodaj umiejętność
    public Map<String, Object> addSkill(AddSkillRequest cpRedSkills) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy użytkownik ma rolę ADMIN
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do dodawania umiejętności.");
        }

        // Sprawdź, czy wszystkie wymagane pola są wypełnione
        if (cpRedSkills.getCategory() == null || cpRedSkills.getName() == null ||
                cpRedSkills.getConnectedStatId() == null || cpRedSkills.getDescription() == null) {
            throw new IllegalStateException("Wszystkie pola muszą być wypełnione.");
        }

        // Sprawdź, czy umiejętność już istnieje
        if (cpRedSkillsRepository.existsByName(cpRedSkills.getName())) {
            throw new IllegalStateException("Umiejętność o tej samej nazwie już istnieje.");
        }
        // Sprawdza, czy nazwa umiejętności nie jest pusta lub składa się tylko z białych znaków
        if (cpRedSkills.getName().isEmpty() || cpRedSkills.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa umiejętności nie może być pusta.");
        }
        // Sprawdź długość nazwy umiejętności
        if (cpRedSkills.getName().length() > 255) {
            throw new IllegalStateException("Nazwa umiejętności nie może być dłuższa niż 50 znaków.");
        }

        // Sprawdza, czy opis umiejętności nie jest pusty lub składa się tylko z białych znaków
        if (cpRedSkills.getDescription().isEmpty() || cpRedSkills.getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Opis umiejętności nie może być pusty.");
        }
        // Sprawdź długość opisu umiejętności
        if (cpRedSkills.getDescription().length() > 500) {
            throw new IllegalStateException("Opis umiejętności nie może być dłuższy niż 255 znaków.");
        }

        // Sprawdzenie, czy id powiązanej statystyki istnieje
        CpRedStats connectedStat = cpRedStatsRepository.findById(cpRedSkills.getConnectedStatId())
                .orElseThrow(() -> new ResourceNotFoundException("Statystyka o podanym id nie istnieje."));

        // Zapisz nową umiejętność
        CpRedSkills newSkill = new CpRedSkills(
                null,
                cpRedSkills.getCategory(),
                cpRedSkills.getName(),
                connectedStat,
                cpRedSkills.getDescription()
        );

        cpRedSkillsRepository.save(newSkill);

        return CustomReturnables.getOkResponseMap("Umiejętność została dodana.");
    }

    // Modyfikuj umiejętność
    public Map<String, Object> updateSkill(Long id, AddSkillRequest cpRedSkills) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy użytkownik ma rolę ADMIN
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do modyfikacji umiejętności.");
        }

        // Sprawdź, czy umiejętność istnieje
        CpRedSkills skillToUpdate = cpRedSkillsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Umiejętność o id " + id + " nie istnieje"));

        // Sprawdzenie nazwy umiejętności
        if (cpRedSkills.getName() != null) {
            // Sprawdź, czy umiejętność już istnieje
            if (cpRedSkillsRepository.existsByName(cpRedSkills.getName())) {
                throw new IllegalStateException("Umiejętność o tej samej nazwie już istnieje.");
            }
            // Sprawdza, czy nazwa umiejętności nie jest pusta lub składa się tylko z białych znaków
            if (cpRedSkills.getName().isEmpty() || cpRedSkills.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa umiejętności nie może być pusta.");
            }
            // Sprawdź długość nazwy umiejętności
            if (cpRedSkills.getName().length() > 255) {
                throw new IllegalStateException("Nazwa umiejętności nie może być dłuższa niż 50 znaków.");
            }
            skillToUpdate.setName(cpRedSkills.getName());
        }

        // Sprawdzenie opisu umiejętności
        if (cpRedSkills.getDescription() != null) {
            // Sprawdza, czy opis umiejętności nie jest pusty lub składa się tylko z białych znaków
            if (cpRedSkills.getDescription().isEmpty() || cpRedSkills.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis umiejętności nie może być pusty.");
            }
            // Sprawdź długość opisu umiejętności
            if (cpRedSkills.getDescription().length() > 500) {
                throw new IllegalStateException("Opis umiejętności nie może być dłuższy niż 255 znaków.");
            }
            skillToUpdate.setDescription(cpRedSkills.getDescription());
        }

        // Sprawdzenie powiązanej statystyki
        if (cpRedSkills.getConnectedStatId() != null) {
            // Sprawdzenie, czy id powiązanej statystyki istnieje
            CpRedStats connectedStat = cpRedStatsRepository.findById(cpRedSkills.getConnectedStatId())
                    .orElseThrow(() -> new ResourceNotFoundException("Statystyka o podanym id nie istnieje."));
            skillToUpdate.setConnectedStat(connectedStat);
        }

        // Sprawdzenie kategorii umiejętności
        if (cpRedSkills.getCategory() != null) {
            skillToUpdate.setCategory(cpRedSkills.getCategory());
        }

        // Zapisz zmodyfikowaną umiejętność
        cpRedSkillsRepository.save(skillToUpdate);

        return CustomReturnables.getOkResponseMap("Umiejętność została zmodyfikowana.");
    }

}

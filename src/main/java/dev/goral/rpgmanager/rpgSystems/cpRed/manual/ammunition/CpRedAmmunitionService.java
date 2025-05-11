package dev.goral.rpgmanager.rpgSystems.cpRed.manual.ammunition;

import dev.goral.rpgmanager.rpgSystems.cpRed.items.CpRedItemsAvailability;
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
public class CpRedAmmunitionService {
    private final CpRedAmmunitionRepository cpRedAmmunitionRepository;
    private UserRepository userRepository;

    // Pobierz wszystkie amunicje
    public List<CpRedAmmunitionDTO> getAllAmmunition(){
        List<CpRedAmmunition> cpRedAmmunitionList = cpRedAmmunitionRepository.findAll();
        return cpRedAmmunitionList.stream()
                .map(cpRedAmmunition -> new CpRedAmmunitionDTO(
                        cpRedAmmunition.getName(),
                        cpRedAmmunition.getDescription(),
                        cpRedAmmunition.getPricePerBullet(),
                        cpRedAmmunition.getAvailability().toString()
                )).toList();
    }

    // Pobierz amunicje po id
    public CpRedAmmunitionDTO getAmmunitionById(Long ammunitionId){
        return cpRedAmmunitionRepository.findById(ammunitionId)
                .map(cpRedAmmunition -> new CpRedAmmunitionDTO(
                        cpRedAmmunition.getName(),
                        cpRedAmmunition.getDescription(),
                        cpRedAmmunition.getPricePerBullet(),
                        cpRedAmmunition.getAvailability().toString()
                )).orElseThrow(() -> new ResourceNotFoundException("Amunicja o id " + ammunitionId + " nie istnieje"));
    }

    // Pobierz wszystkie amunicje dla admina
    public List<CpRedAmmunition> getAllAmmunitionForAdmin(){
        return cpRedAmmunitionRepository.findAll();
    }

    // Dodaj amunicje
    public Map<String, Object> addAmmunition(CpRedAmmunition cpRedAmmunition){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy użytkownik ma rolę ADMIN
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do dodawania amunicji.");
        }

        // Sprawdź, czy wszystkie pola są wypełnione
        if (cpRedAmmunition.getName() == null || cpRedAmmunition.getDescription() == null || cpRedAmmunition.getAvailability() == null) {
            throw new IllegalStateException("Wszystkie pola muszą być wypełnione.");
        }

        // Sprawdź, czy amunicja o podanej nazwie już istnieje
        if (cpRedAmmunitionRepository.existsByName(cpRedAmmunition.getName())) {
            throw new IllegalStateException("Amunicja o tej nazwie już istnieje.");
        }
        // Sprawdzenie, czy nazwa nie jest pusta lub skłąda się tylko z białych znaków
        if (cpRedAmmunition.getName().isEmpty() || cpRedAmmunition.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa amunicji nie może być pusta.");
        }
        // Sprawdź długość nazwy
        if (cpRedAmmunition.getName().length() > 255) {
            throw new IllegalStateException("Nazwa amunicji jest za długa. Maksymalna długość to 255 znaków.");
        }

        // Sprawdź, czy cena za pocisk jest większa od 0
        if (cpRedAmmunition.getPricePerBullet() <= 0) {
            throw new IllegalStateException("Cena za pocisk musi być większa od 0.");
        }

        // Sprawdź długość opisu
        if (cpRedAmmunition.getDescription().length() > 500) {
            throw new IllegalStateException("Opis amunicji jest za długi. Maksymalna długość to 500 znaków.");
        }
        // Sprawdzenie, czy opis nie jest pusty lub składa się tylko z białych znaków
        if (cpRedAmmunition.getDescription().isEmpty() || cpRedAmmunition.getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Opis amunicji nie może być pusty.");
        }

        // Sprawdzenie, czy dostępność jest jedną z dostępnych opcji
        String inputAvailability = cpRedAmmunition.getAvailability().toString();
        try {
            CpRedItemsAvailability availability = CpRedItemsAvailability.valueOf(inputAvailability.toUpperCase());
            // Jeśli wartość jest poprawna, możesz kontynuować
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Podana dostępność nie jest jedną z dostępnych opcji.");
        }

        // Zapisanie amunicji do bazy danych
        cpRedAmmunitionRepository.save(cpRedAmmunition);
        return CustomReturnables.getOkResponseMap("Amunicja została dodana.");
    }

    // Modyfikuj amunicje
    public Map<String, Object> updateAmmunition(Long ammunitionId, CpRedAmmunition cpRedAmmunition){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy użytkownik ma rolę ADMIN
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do modyfikacji amunicji.");
        }

        // Sprawdź, czy amunicja o podanym id istnieje
        CpRedAmmunition ammunitionToUpdate = cpRedAmmunitionRepository.findById(ammunitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Amunicja o id " + ammunitionId + " nie istnieje"));

        // Sprawdzenie nazwy
        if (cpRedAmmunition.getName() != null) {
            // Sprawdzenie, czy amunicja o podanej nazwie już istnieje
            if (cpRedAmmunitionRepository.existsByName(cpRedAmmunition.getName())) {
                throw new IllegalStateException("Amunicja o tej nazwie już istnieje.");
            }
            // Sprawdzenie, czy nazwa nie jest pusta lub składa się tylko z białych znaków
            if (cpRedAmmunition.getName().isEmpty() || cpRedAmmunition.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa amunicji nie może być pusta.");
            }
            // Sprawdź długość nazwy
            if (cpRedAmmunition.getName().length() > 255) {
                throw new IllegalStateException("Nazwa amunicji jest za długa. Maksymalna długość to 255 znaków.");
            }
            ammunitionToUpdate.setName(cpRedAmmunition.getName());
        }

        // Sprawdzenie opisu
        if (cpRedAmmunition.getDescription() != null) {
            // Sprawdź długość opisu
            if (cpRedAmmunition.getDescription().length() > 500) {
                throw new IllegalStateException("Opis amunicji jest za długi. Maksymalna długość to 500 znaków.");
            }
            // Sprawdzenie, czy opis nie jest pusty lub składa się tylko z białych znaków
            if (cpRedAmmunition.getDescription().isEmpty() || cpRedAmmunition.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis amunicji nie może być pusty.");
            }
            ammunitionToUpdate.setDescription(cpRedAmmunition.getDescription());
        }

        // Sprawdzenie ceny
        if (cpRedAmmunition.getPricePerBullet() != ammunitionToUpdate.getPricePerBullet()) {
            if (cpRedAmmunition.getPricePerBullet() != -1){
                if (cpRedAmmunition.getPricePerBullet() <= 0) {
                    throw new IllegalStateException("Cena za pocisk musi być większa od 0.");
                }
                ammunitionToUpdate.setPricePerBullet(cpRedAmmunition.getPricePerBullet());
            }
        }

        // Sprawdzenie dostępności
        if (cpRedAmmunition.getAvailability() != null) {
            String inputAvailability = cpRedAmmunition.getAvailability().toString();
            try {
                CpRedItemsAvailability availability = CpRedItemsAvailability.valueOf(inputAvailability.toUpperCase());
                // Jeśli wartość jest poprawna, możesz kontynuować
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Podana dostępność nie jest jedną z dostępnych opcji.");
            }
            ammunitionToUpdate.setAvailability(cpRedAmmunition.getAvailability());
        }

        // Zapisanie zmodyfikowanej amunicji do bazy danych
        cpRedAmmunitionRepository.save(ammunitionToUpdate);

        return CustomReturnables.getOkResponseMap("Amunicja została zmodyfikowana.");
    }
}

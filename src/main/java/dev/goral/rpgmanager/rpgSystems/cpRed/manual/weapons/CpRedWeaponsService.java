package dev.goral.rpgmanager.rpgSystems.cpRed.manual.weapons;

import dev.goral.rpgmanager.rpgSystems.cpRed.manual.ammunition.CpRedAmmunition;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.ammunition.CpRedAmmunitionRepository;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.skills.CpRedSkills;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.skills.CpRedSkillsRepository;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.stats.CpRedStatsRepository;
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
public class CpRedWeaponsService {
    private final CpRedWeaponsRepository cpRedWeaponsRepository;
    private final UserRepository userRepository;
    private final CpRedSkillsRepository cpRedSkillsRepository;
    private final CpRedAmmunitionRepository cpRedAmmunitionRepository;

    // Pobierz wszystkie bronie
    public List<CpRedWeaponsDTO> getAllWeapons() {
        List<CpRedWeapons> cpRedWeaponsList = cpRedWeaponsRepository.findAll();
        return cpRedWeaponsList.stream()
                .map(cpRedWeapons -> new CpRedWeaponsDTO(
                        cpRedWeapons.getRequiredSkillId().getId(),
                        cpRedWeapons.getName(),
                        cpRedWeapons.getDamage(),
                        cpRedWeapons.getMagazineCapacity(),
                        cpRedWeapons.getStandardAmmunitionId().getId(),
                        cpRedWeapons.getNumberOfAttacks(),
                        cpRedWeapons.getHandType(),
                        cpRedWeapons.isHidden(),
                        cpRedWeapons.getQuality().toString(),
                        cpRedWeapons.getPrice(),
                        cpRedWeapons.getAvailability().toString(),
                        cpRedWeapons.isModifiable(),
                        cpRedWeapons.getDescription()
                )).toList();
    }

    // Pobierz broń po id
    public CpRedWeaponsDTO getWeaponById(Long weaponId) {
        return cpRedWeaponsRepository.findById(weaponId)
                .map(cpRedWeapons -> new CpRedWeaponsDTO(
                        cpRedWeapons.getRequiredSkillId().getId(),
                        cpRedWeapons.getName(),
                        cpRedWeapons.getDamage(),
                        cpRedWeapons.getMagazineCapacity(),
                        cpRedWeapons.getStandardAmmunitionId().getId(),
                        cpRedWeapons.getNumberOfAttacks(),
                        cpRedWeapons.getHandType(),
                        cpRedWeapons.isHidden(),
                        cpRedWeapons.getQuality().toString(),
                        cpRedWeapons.getPrice(),
                        cpRedWeapons.getAvailability().toString(),
                        cpRedWeapons.isModifiable(),
                        cpRedWeapons.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Broń o id " + weaponId + " nie istnieje"));
    }

    // Pobierz wszystkie bronie dla admina
    public List<CpRedWeapons> getAllWeaponsForAdmin() {
        return cpRedWeaponsRepository.findAll();
    }

    // Dodać broń
    public Map<String, Object> addWeapon(AddWeaponRequest cpRedWeapons) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy użytkownik ma rolę ADMIN
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do dodawania broni.");
        }

        // Sprawdź, czy wszystkie wymagane pola są wypełnione
        if (cpRedWeapons.getRequiredSkillId() == null || cpRedWeapons.getName() == null ||
                cpRedWeapons.getDamage() == -1 || cpRedWeapons.getMagazineCapacity() == -1 ||
                cpRedWeapons.getStandardAmmunitionId() == null || cpRedWeapons.getNumberOfAttacks() == -1 ||
                cpRedWeapons.getHandType() == -1 || cpRedWeapons.getQuality() == null ||
                cpRedWeapons.getPrice() == -1 || cpRedWeapons.getAvailability() == null ||
                cpRedWeapons.getDescription() == null) {
            throw new IllegalStateException("Nie wszystkie pola zostały wypełnione.");
        }

        // Sprawdź, czy broń o tej samej nazwie już istnieje
        if (cpRedWeaponsRepository.existsByName(cpRedWeapons.getName())) {
            throw new IllegalStateException("Broń o tej samej nazwie już istnieje.");
        }
        // Sprawdza, czy nazwa nie jest pusta lub składa się tylko z białych znaków
        if (cpRedWeapons.getName().isEmpty() || cpRedWeapons.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa broni nie może być pusta.");
        }
        // Sprawdź długość nazwa broni
        if (cpRedWeapons.getName().length() > 255) {
            throw new IllegalStateException("Nazwa broni nie może być dłuższa niż 255 znaków.");
        }
        // Sprawdź, czy umiejętność o podanym id istnieje
        CpRedSkills requiredSkill = cpRedSkillsRepository.findById(cpRedWeapons.getRequiredSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Umiejętność o id " + cpRedWeapons.getRequiredSkillId() + " nie istnieje"));
        // Sprawdź, czy dmg nie jest ujemne
        if (cpRedWeapons.getDamage() < 0) {
            throw new IllegalStateException("Dmg nie może być ujemne.");
        }
        // Sprawdź, czy pojemność magazynka nie jest ujemna
        if (cpRedWeapons.getMagazineCapacity() < 0) {
            throw new IllegalStateException("Pojemność magazynka nie może być ujemna.");
        }
        // Sprawdź, czy amunicja o podanym id istnieje
        CpRedAmmunition standardAmmunition = cpRedAmmunitionRepository.findById(cpRedWeapons.getStandardAmmunitionId())
                .orElseThrow(() -> new ResourceNotFoundException("Amunicja o id " + cpRedWeapons.getStandardAmmunitionId() + " nie istnieje"));
        // Sprawdź, czy liczba ataków jest większa od 0
        if (cpRedWeapons.getNumberOfAttacks() <= 0) {
            throw new IllegalStateException("Liczba ataków musi być większa od 0.");
        }
        // Sprawdź, czy typ ręki jest większy od 0
        if (cpRedWeapons.getHandType() <= 0) {
            throw new IllegalStateException("Typ ręki musi być większy od 0.");
        }
        // Sprawdź, czy cena nie jest ujemna
        if (cpRedWeapons.getPrice() < 0) {
            throw new IllegalStateException("Cena nie może być ujemna.");
        }
        // Sprawdź, czy opis nie jest pusty lub składa się tylko z białych znaków
        if (cpRedWeapons.getDescription().isEmpty() || cpRedWeapons.getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Opis broni nie może być pusty.");
        }
        // Sprawdź długość opisu
        if (cpRedWeapons.getDescription().length() > 500) {
            throw new IllegalStateException("Opis broni nie może być dłuższy niż 500 znaków.");
        }

        // Zapisanie broni
        CpRedWeapons newWeapon = new CpRedWeapons(
                null,
                requiredSkill,
                cpRedWeapons.getName(),
                cpRedWeapons.getDamage(),
                cpRedWeapons.getMagazineCapacity(),
                standardAmmunition,
                cpRedWeapons.getNumberOfAttacks(),
                cpRedWeapons.getHandType(),
                cpRedWeapons.isHidden(),
                cpRedWeapons.getQuality(),
                cpRedWeapons.getPrice(),
                cpRedWeapons.getAvailability(),
                cpRedWeapons.isModifiable(),
                cpRedWeapons.getDescription()
        );

        cpRedWeaponsRepository.save(newWeapon);
        return CustomReturnables.getOkResponseMap("Broń została dodana.");
    }

    // Modyfikować broń
    public Map<String, Object> updateWeapon(Long weaponId, AddWeaponRequest cpRedWeapons) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy użytkownik ma rolę ADMIN
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do dodawania broni.");
        }

        // Sprawdź, czy broń o podanym id istnieje
        CpRedWeapons weaponToUpdate = cpRedWeaponsRepository.findById(weaponId)
                .orElseThrow(() -> new ResourceNotFoundException("Broń o id " + weaponId + " nie istnieje"));

        // Sprawdzenie nazwy
        if(cpRedWeapons.getName() != null) {
            // Sprawdź, czy broń o tej samej nazwie już istnieje
            if (cpRedWeaponsRepository.existsByName(cpRedWeapons.getName())) {
                throw new IllegalStateException("Broń o tej samej nazwie już istnieje.");
            }
            // Sprawdza, czy nazwa nie jest pusta lub składa się tylko z białych znaków
            if (cpRedWeapons.getName().isEmpty() || cpRedWeapons.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa broni nie może być pusta.");
            }
            // Sprawdź długość nazwa broni
            if (cpRedWeapons.getName().length() > 255) {
                throw new IllegalStateException("Nazwa broni nie może być dłuższa niż 255 znaków.");
            }
            weaponToUpdate.setName(cpRedWeapons.getName());
        }

        // Sprawdzenie umiejętności
        if(cpRedWeapons.getRequiredSkillId() != null) {
            // Sprawdź, czy umiejętność o podanym id istnieje
            CpRedSkills requiredSkill = cpRedSkillsRepository.findById(cpRedWeapons.getRequiredSkillId())
                    .orElseThrow(() -> new ResourceNotFoundException("Umiejętność o id " + cpRedWeapons.getRequiredSkillId() + " nie istnieje"));
            weaponToUpdate.setRequiredSkillId(requiredSkill);
        }

        // Sprawdzenie obrażeń
        if(cpRedWeapons.getDamage() != -1) {
            // Sprawdź, czy dmg nie jest ujemne
            if (cpRedWeapons.getDamage() < 0) {
                throw new IllegalStateException("Dmg nie może być ujemne.");
            }
            weaponToUpdate.setDamage(cpRedWeapons.getDamage());
        }

        // Sprawdzenie pojemności magazynka
        if(cpRedWeapons.getMagazineCapacity() != -1) {
            // Sprawdź, czy pojemność magazynka nie jest ujemna
            if (cpRedWeapons.getMagazineCapacity() < 0) {
                throw new IllegalStateException("Pojemność magazynka nie może być ujemna.");
            }
            weaponToUpdate.setMagazineCapacity(cpRedWeapons.getMagazineCapacity());
        }

        // Sprawdzenie amunicji
        if(cpRedWeapons.getStandardAmmunitionId() != null) {
            // Sprawdź, czy amunicja o podanym id istnieje
            CpRedAmmunition standardAmmunition = cpRedAmmunitionRepository.findById(cpRedWeapons.getStandardAmmunitionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Amunicja o id " + cpRedWeapons.getStandardAmmunitionId() + " nie istnieje"));
            weaponToUpdate.setStandardAmmunitionId(standardAmmunition);
        }

        // Sprawdzenie liczby ataków
        if(cpRedWeapons.getNumberOfAttacks() != -1) {
            // Sprawdź, czy liczba ataków jest większa od 0
            if (cpRedWeapons.getNumberOfAttacks() <= 0) {
                throw new IllegalStateException("Liczba ataków musi być większa od 0.");
            }
            weaponToUpdate.setNumberOfAttacks(cpRedWeapons.getNumberOfAttacks());
        }

        // Sprawdzenie typu ręki
        if(cpRedWeapons.getHandType() != -1) {
            // Sprawdź, czy typ ręki jest większy od 0
            if (cpRedWeapons.getHandType() <= 0) {
                throw new IllegalStateException("Typ ręki musi być większy od 0.");
            }
            weaponToUpdate.setHandType(cpRedWeapons.getHandType());
        }

        // Sprawdzenie jakości
        if(cpRedWeapons.getQuality() != null) {
            weaponToUpdate.setQuality(cpRedWeapons.getQuality());
        }

        // Sprawdzenie ceny
        if(cpRedWeapons.getPrice() != -1) {
            // Sprawdź, czy cena nie jest ujemna
            if (cpRedWeapons.getPrice() < 0) {
                throw new IllegalStateException("Cena nie może być ujemna.");
            }
            weaponToUpdate.setPrice(cpRedWeapons.getPrice());
        }

        // Sprawdzenie dostępności
        if(cpRedWeapons.getAvailability() != null) {
            weaponToUpdate.setAvailability(cpRedWeapons.getAvailability());
        }

        // Sprawdzenie opisu
        if(cpRedWeapons.getDescription() != null) {
            // Sprawdź długość opisu
            if (cpRedWeapons.getDescription().length() > 500) {
                throw new IllegalStateException("Opis broni nie może być dłuższy niż 500 znaków.");
            }
            // Sprawdzenie, czy opis nie jest pusty lub składa się tylko z białych znaków
            if (cpRedWeapons.getDescription().isEmpty() || cpRedWeapons.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis broni nie może być pusty.");
            }
            weaponToUpdate.setDescription(cpRedWeapons.getDescription());
        }

        // Zapisanie zmodyfikowanej broni
        cpRedWeaponsRepository.save(weaponToUpdate);

        return CustomReturnables.getOkResponseMap("Broń została zmodyfikowana.");
    }

    // Zmienić ukrywalność broni
    public Map<String, Object> hideWeapon(Long weaponId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy użytkownik ma rolę ADMIN
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do modyfikacji broni.");
        }

        // Sprawdź, czy broń o podanym id istnieje
        CpRedWeapons weaponToUpdate = cpRedWeaponsRepository.findById(weaponId)
                .orElseThrow(() -> new ResourceNotFoundException("Broń o id " + weaponId + " nie istnieje"));

        // Zmiana ukrywalności broni
        weaponToUpdate.setHidden(!weaponToUpdate.isHidden());
        cpRedWeaponsRepository.save(weaponToUpdate);

        return CustomReturnables.getOkResponseMap("Ukrywalność broni została zmieniona.");
    }

    // Zmienić modyfikowalność broni
    public Map<String, Object> modifiableWeapon(Long weaponId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy użytkownik ma rolę ADMIN
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do modyfikacji broni.");
        }

        // Sprawdź, czy broń o podanym id istnieje
        CpRedWeapons weaponToUpdate = cpRedWeaponsRepository.findById(weaponId)
                .orElseThrow(() -> new ResourceNotFoundException("Broń o id " + weaponId + " nie istnieje"));

        // Zmiana modyfikowalności broni
        weaponToUpdate.setModifiable(!weaponToUpdate.isModifiable());
        cpRedWeaponsRepository.save(weaponToUpdate);

        return CustomReturnables.getOkResponseMap("Modyfikowalność broni została zmieniona.");
    }
}

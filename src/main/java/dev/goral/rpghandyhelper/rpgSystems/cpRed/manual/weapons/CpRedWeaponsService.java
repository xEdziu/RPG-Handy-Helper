package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition.CpRedAmmunition;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition.CpRedAmmunitionRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.skills.CpRedSkills;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.skills.CpRedSkillsRepository;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import dev.goral.rpghandyhelper.user.UserRole;
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
    public Map<String, Object> getAllWeapons() {
        List<CpRedWeaponsDTO> cpRedWeaponsList = cpRedWeaponsRepository.findAll().stream()
                .map(cpRedWeapons -> new CpRedWeaponsDTO(
                        cpRedWeapons.getRequiredSkillId().getId(),
                        cpRedWeapons.getName(),
                        cpRedWeapons.getDamage(),
                        cpRedWeapons.getMagazineCapacity(),
                        cpRedWeapons.getStandardAmmunitionId().getId(),
                        cpRedWeapons.getNumberOfAttacks(),
                        cpRedWeapons.getHandType(),
                        cpRedWeapons.getIsHidden(),
                        cpRedWeapons.getQuality().toString(),
                        cpRedWeapons.getPrice(),
                        cpRedWeapons.getAvailability().toString(),
                        cpRedWeapons.getIsModifiable(),
                        cpRedWeapons.getModSlots(),
                        cpRedWeapons.getDescription()
                )).toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Bronie zostały pobrane.");
        response.put("weapons", cpRedWeaponsList);
        return response;
    }

    // Pobierz broń po id
    public Map<String, Object> getWeaponById(Long weaponId) {
        CpRedWeaponsDTO weapon = cpRedWeaponsRepository.findById(weaponId)
                .map(cpRedWeapons -> new CpRedWeaponsDTO(
                        cpRedWeapons.getRequiredSkillId().getId(),
                        cpRedWeapons.getName(),
                        cpRedWeapons.getDamage(),
                        cpRedWeapons.getMagazineCapacity(),
                        cpRedWeapons.getStandardAmmunitionId().getId(),
                        cpRedWeapons.getNumberOfAttacks(),
                        cpRedWeapons.getHandType(),
                        cpRedWeapons.getIsHidden(),
                        cpRedWeapons.getQuality().toString(),
                        cpRedWeapons.getPrice(),
                        cpRedWeapons.getAvailability().toString(),
                        cpRedWeapons.getIsModifiable(),
                        cpRedWeapons.getModSlots(),
                        cpRedWeapons.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Broń o id " + weaponId + " nie istnieje"));

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Broń została pobrana.");
        response.put("weapon", weapon);
        return response;
    }

    // Pobierz wszystkie bronie dla admina
    public Map<String, Object> getAllWeaponsForAdmin() {
        List<CpRedWeapons> allWeaponsList = cpRedWeaponsRepository.findAll();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Bronie zostały pobrane dla administratora.");
        response.put("weapons", allWeaponsList);
        return response;
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
                cpRedWeapons.getDamage() == null || cpRedWeapons.getMagazineCapacity() == null ||
                cpRedWeapons.getStandardAmmunitionId() == null || cpRedWeapons.getNumberOfAttacks() == null ||
                cpRedWeapons.getHandType() == null || cpRedWeapons.getIsHidden() == null ||cpRedWeapons.getQuality() == null ||
                cpRedWeapons.getPrice() == null || cpRedWeapons.getAvailability() == null ||
                cpRedWeapons.getIsModifiable() == null || cpRedWeapons.getModSlots() == null ||
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
        // Sprawdzenie ilości modyfikacji
        if (!cpRedWeapons.getIsModifiable()){
            cpRedWeapons.setModSlots((short)0);
        } else {
                if (cpRedWeapons.getModSlots() == null || cpRedWeapons.getModSlots() < 0) {
                throw new IllegalStateException("Ilość modyfikacji musi być większa lub równa 0.");
            }
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
                cpRedWeapons.getIsHidden(),
                cpRedWeapons.getQuality(),
                cpRedWeapons.getPrice(),
                cpRedWeapons.getAvailability(),
                cpRedWeapons.getIsModifiable(),
                cpRedWeapons.getModSlots(),
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
        if(cpRedWeapons.getDamage() != null) {
            // Sprawdź, czy dmg nie jest ujemne
            if (cpRedWeapons.getDamage() < 0) {
                throw new IllegalStateException("Dmg nie może być ujemne.");
            }
            weaponToUpdate.setDamage(cpRedWeapons.getDamage());
        }

        // Sprawdzenie pojemności magazynka
        if(cpRedWeapons.getMagazineCapacity() != null) {
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
        if(cpRedWeapons.getNumberOfAttacks() != null) {
            // Sprawdź, czy liczba ataków jest większa od 0
            if (cpRedWeapons.getNumberOfAttacks() <= 0) {
                throw new IllegalStateException("Liczba ataków musi być większa od 0.");
            }
            weaponToUpdate.setNumberOfAttacks(cpRedWeapons.getNumberOfAttacks());
        }

        // Sprawdzenie typu ręki
        if(cpRedWeapons.getHandType() != null) {
            // Sprawdź, czy typ ręki jest większy od 0
            if (cpRedWeapons.getHandType() <= 0) {
                throw new IllegalStateException("Typ ręki musi być większy od 0.");
            }
            weaponToUpdate.setHandType(cpRedWeapons.getHandType());
        }

        // Sprawdzenie, czy broń jest ukryta
        if(cpRedWeapons.getIsHidden() != null) {
            weaponToUpdate.setIsHidden(cpRedWeapons.getIsHidden());
        }

        // Sprawdzenie jakości
        if(cpRedWeapons.getQuality() != null) {
            weaponToUpdate.setQuality(cpRedWeapons.getQuality());
        }

        // Sprawdzenie ceny
        if(cpRedWeapons.getPrice() != null) {
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

        // Sprawdzenie modyfikowalności
        if(cpRedWeapons.getIsModifiable() != null) {
            weaponToUpdate.setIsModifiable(cpRedWeapons.getIsModifiable());
        }

        // Sprawdzenie ilości modyfikacji
        if(cpRedWeapons.getModSlots() != null) {
            if (!weaponToUpdate.getIsModifiable()){
                cpRedWeapons.setModSlots((short)0);
            } else {
                if (cpRedWeapons.getModSlots() < 0) {
                    throw new IllegalStateException("Ilość modyfikacji musi być większa lub równa 0.");
                }
            }
            weaponToUpdate.setModSlots(cpRedWeapons.getModSlots());
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

}

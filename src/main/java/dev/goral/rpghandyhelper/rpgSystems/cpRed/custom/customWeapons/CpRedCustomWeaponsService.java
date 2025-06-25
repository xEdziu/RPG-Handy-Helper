package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeapons;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.skills.CpRedSkills;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.skills.CpRedSkillsRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons.CpRedWeaponsType;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomWeaponsService {
    private final CpRedCustomWeaponsRepository cpRedCustomWeaponsRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedSkillsRepository cpRedSkillsRepository;

    // Pobierz wszystkie customowe bronie
    public Map<String, Object> getAllCustomWeapons() {
        List<CpRedCustomWeaponsDTO> allCustomWeapons = cpRedCustomWeaponsRepository.findAll().stream()
                .map(cpRedCustomWeapons -> new CpRedCustomWeaponsDTO(
                        cpRedCustomWeapons.getId(),
                        cpRedCustomWeapons.getGameId().getId(),
                        cpRedCustomWeapons.getName(),
                        cpRedCustomWeapons.getRequiredSkillId().getId(),
                        cpRedCustomWeapons.getType().name(),
                        cpRedCustomWeapons.getDamage(),
                        cpRedCustomWeapons.getMagazineCapacity(),
                        cpRedCustomWeapons.getNumberOfAttacks(),
                        cpRedCustomWeapons.getHandType(),
                        cpRedCustomWeapons.getIsHidden(),
                        cpRedCustomWeapons.getQuality().name(),
                        cpRedCustomWeapons.getPrice(),
                        cpRedCustomWeapons.getAvailability().name(),
                        cpRedCustomWeapons.getIsModifiable(),
                        cpRedCustomWeapons.getModSlots(),
                        cpRedCustomWeapons.getDescription()
                )).toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowa broń pobrana pomyślnie");
        response.put("customWeapons", allCustomWeapons);
        return response;
    }

    // Pobierz customową broń po id
    public Map<String, Object> getCustomWeaponById(Long customWeaponId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy customowa broń o podanym id istnieje
        CpRedCustomWeapons weaponToGet = cpRedCustomWeaponsRepository.findById(customWeaponId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowa broń o id " + customWeaponId + " nie istnieje"));

        // Sprawdź, czy gra istnieje
        Game game = gameRepository.findById(weaponToGet.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + weaponToGet.getGameId().getId() + " nie istnieje."));

        // Sprawdź, czy użytkownik należy do tej gry
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie jesteś graczem wybranej gry."));

        CpRedCustomWeaponsDTO weaponsDTO = cpRedCustomWeaponsRepository.findById(customWeaponId)
                .map(cpRedCustomWeapons -> new CpRedCustomWeaponsDTO(
                        cpRedCustomWeapons.getId(),
                        cpRedCustomWeapons.getGameId().getId(),
                        cpRedCustomWeapons.getName(),
                        cpRedCustomWeapons.getRequiredSkillId().getId(),
                        cpRedCustomWeapons.getType().name(),
                        cpRedCustomWeapons.getDamage(),
                        cpRedCustomWeapons.getMagazineCapacity(),
                        cpRedCustomWeapons.getNumberOfAttacks(),
                        cpRedCustomWeapons.getHandType(),
                        cpRedCustomWeapons.getIsHidden(),
                        cpRedCustomWeapons.getQuality().name(),
                        cpRedCustomWeapons.getPrice(),
                        cpRedCustomWeapons.getAvailability().name(),
                        cpRedCustomWeapons.getIsModifiable(),
                        cpRedCustomWeapons.getModSlots(),
                        cpRedCustomWeapons.getDescription()
                )).orElseThrow(() -> new RuntimeException("Customowa broń o id " + customWeaponId + " nie istnieje"));

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowa broń pobrana pomyślnie");
        response.put("customWeapon", weaponsDTO);
        return response;
    }

    // Pobierz wszystkie customowe bronie danej gry
    public Map<String, Object> getAllCustomWeaponsByGameId(Long gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy gra istnieje
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + gameId + " nie istnieje."));

        // Sprawdź, czy użytkownik należy do tej gry
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie jesteś graczem wybranej gry."));


        List<CpRedCustomWeaponsDTO> allCustomWeapons = cpRedCustomWeaponsRepository.findAllByGameId_Id(gameId).stream()
                .map(cpRedCustomWeapons -> new CpRedCustomWeaponsDTO(
                        cpRedCustomWeapons.getId(),
                        cpRedCustomWeapons.getGameId().getId(),
                        cpRedCustomWeapons.getName(),
                        cpRedCustomWeapons.getRequiredSkillId().getId(),
                        cpRedCustomWeapons.getType().name(),
                        cpRedCustomWeapons.getDamage(),
                        cpRedCustomWeapons.getMagazineCapacity(),
                        cpRedCustomWeapons.getNumberOfAttacks(),
                        cpRedCustomWeapons.getHandType(),
                        cpRedCustomWeapons.getIsHidden(),
                        cpRedCustomWeapons.getQuality().name(),
                        cpRedCustomWeapons.getPrice(),
                        cpRedCustomWeapons.getAvailability().name(),
                        cpRedCustomWeapons.getIsModifiable(),
                        cpRedCustomWeapons.getModSlots(),
                        cpRedCustomWeapons.getDescription()
                )).toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe bronie pobrane pomyślnie");
        response.put("customWeapons", allCustomWeapons);
        return response;
    }

    // Dodaj customową broń
    public Map<String, Object> addCustomWeapon(AddCustomWeaponRequest addCustomWeaponRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy wszystkie wymagane pola są wypełnione
        if (addCustomWeaponRequest.getGameId() == null ||
                addCustomWeaponRequest.getRequiredSkillId() == null ||
                addCustomWeaponRequest.getName() == null ||
                addCustomWeaponRequest.getType() == null ||
                addCustomWeaponRequest.getDamage() == null ||
                addCustomWeaponRequest.getMagazineCapacity() == null ||
                addCustomWeaponRequest.getNumberOfAttacks() == null ||
                addCustomWeaponRequest.getHandType() == null ||
                addCustomWeaponRequest.getIsHidden() == null ||
                addCustomWeaponRequest.getQuality() == null ||
                addCustomWeaponRequest.getPrice() == null ||
                addCustomWeaponRequest.getAvailability() == null ||
                addCustomWeaponRequest.getIsModifiable() == null ||
                addCustomWeaponRequest.getModSlots() == null ||
                addCustomWeaponRequest.getDescription() == null) {
            throw new IllegalStateException("Nie wszystkie pola zostały wypełnione.");
        }

        // Sprawdź, czy podana gra istnieje
        Game game = gameRepository.findById(addCustomWeaponRequest.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + addCustomWeaponRequest.getGameId() + " nie istnieje."));

        // Sprawdź, czy gra jest aktywna
        if(game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + addCustomWeaponRequest.getGameId() + " jest nie aktywna.");
        }

        // Sprawdź, czy użytkownik należy do tej gry
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), addCustomWeaponRequest.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));

        // Sprawdź, czy użytkownik jest GM
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może dodać broń do gry.");
        }

        // Sprawdź, czy umiejętność o podanym id istnieje
        CpRedSkills requiredSkill = cpRedSkillsRepository.findById(addCustomWeaponRequest.getRequiredSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Umiejętność o id " + addCustomWeaponRequest.getRequiredSkillId() + " nie istnieje"));

        // Sprawdzenie, czy w danej grze jest już broń o tej nazwie już istnieje w tej grze
        if (cpRedCustomWeaponsRepository.existsByNameAndGameId(addCustomWeaponRequest.getName(), game)) {
            throw new IllegalStateException("Customowa broń o tej nazwie już istnieje w tej grze.");
        }

        // Sprawdza, czy nazwa broni nie jest pusta lub składa się tylko z białych znaków
        if (addCustomWeaponRequest.getName().trim().isEmpty() || addCustomWeaponRequest.getName().isEmpty()) {
            throw new IllegalStateException("Nazwa customowej broni nie może być pusta.");
        }

        // Sprawdź długość nazwy broni
        if (addCustomWeaponRequest.getName().length() > 255) {
            throw new IllegalStateException("Nazwa customowej broni nie może być dłuższa niż 255 znaków");
        }

        // Sprawdź, czy dmg nie jest ujemne
        if (addCustomWeaponRequest.getDamage() < 0) {
            throw new IllegalStateException("Dmg nie mogą być ujemne.");
        }

        // Sprawdź, czy pojemność magazynka nie jest ujemna
        if (addCustomWeaponRequest.getMagazineCapacity() < 0) {
            throw new IllegalStateException("Pojemność magazynka nie może być ujemna.");
        }

        // Sprawdź, czy liczba ataków jest większa od 0
        if (addCustomWeaponRequest.getNumberOfAttacks() < 0) {
            throw new IllegalStateException("Liczba ataków nie może być ujemna.");
        }

        // Sprawdź, czy typ ręki jest większy od 0
        if (addCustomWeaponRequest.getHandType() < 0) {
            throw new IllegalStateException("Typ ręki nie może być ujemny.");
        }

        // Sprawdź, czy cena nie jest ujemna
        if (addCustomWeaponRequest.getPrice() < 0) {
            throw new IllegalStateException("Cena nie może być ujemna.");
        }

        // Sprawdź ilość modyfikacji
        if (!addCustomWeaponRequest.getIsModifiable()){
            addCustomWeaponRequest.setModSlots((short)0);
        } else {
            // Sprawdź, czy ilość modyfikacji nie jest ujemna
            if (addCustomWeaponRequest.getModSlots() == null || addCustomWeaponRequest.getModSlots() < 0) {
                throw new IllegalStateException("Ilość modyfikacji nie może być ujemna.");
            }
        }

        // Sprawdź, czy opis nie jest pusty lub składa się tylko z białych znaków
        if (addCustomWeaponRequest.getDescription().trim().isEmpty() || addCustomWeaponRequest.getDescription().isEmpty()) {
            throw new IllegalStateException("Opis customowej broni nie może być pusty.");
        }

        // Sprawdź długość opisu
        if (addCustomWeaponRequest.getDescription().length() > 500) {
            throw new IllegalStateException("Opis customowej broni nie może być dłuższy niż 500 znaków");
        }

        // Zapisanie broni
        CpRedCustomWeapons newCustomWeapon = new CpRedCustomWeapons(
                null,
                game,
                requiredSkill,
                addCustomWeaponRequest.getName(),
                CpRedWeaponsType.valueOf(addCustomWeaponRequest.getType()),
                addCustomWeaponRequest.getDamage(),
                addCustomWeaponRequest.getMagazineCapacity(),
                addCustomWeaponRequest.getNumberOfAttacks(),
                addCustomWeaponRequest.getHandType(),
                addCustomWeaponRequest.getIsHidden(),
                addCustomWeaponRequest.getQuality(),
                addCustomWeaponRequest.getPrice(),
                addCustomWeaponRequest.getAvailability(),
                addCustomWeaponRequest.getIsModifiable(),
                addCustomWeaponRequest.getModSlots(),
                addCustomWeaponRequest.getDescription()
        );

        cpRedCustomWeaponsRepository.save(newCustomWeapon);
        return CustomReturnables.getOkResponseMap("Customowa amunicja została dodana.");
    }

    // Modyfikuj customową broń
    public Map<String, Object> updateCustomWeapon(Long customWeaponId, AddCustomWeaponRequest cpRedCustomWeapon) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy customowa broń o podanym id istnieje
        CpRedCustomWeapons weaponToUpdate = cpRedCustomWeaponsRepository.findById(customWeaponId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowa broń o id " + customWeaponId + " nie istnieje"));

        // Sprawdź, czy gra istnieje
        Game game = gameRepository.findById(weaponToUpdate.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + weaponToUpdate.getGameId().getId() + " nie istnieje."));

        // Sprawdź, czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + weaponToUpdate.getGameId().getId() + " nie jest aktywna.");
        }

        if (weaponToUpdate.getGameId().getId() != cpRedCustomWeapon.getGameId()) {
            throw new IllegalStateException("Nie można zmienić gry dla broni.");
        }

        // Sprawdź, czy użytkownik należy do gry
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), weaponToUpdate.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));

        // Sprawdź, czy użytkownik jest GM
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może modyfikować broń.");
        }

        // Sprawdź przypisaną statystykę
        if (cpRedCustomWeapon.getRequiredSkillId() != null) {
            CpRedSkills requiredSkill = cpRedSkillsRepository.findById(cpRedCustomWeapon.getRequiredSkillId())
                    .orElseThrow(() -> new ResourceNotFoundException("Umiejętność o id " + cpRedCustomWeapon.getRequiredSkillId() + " nie istnieje"));
            weaponToUpdate.setRequiredSkillId(requiredSkill);
        }

        // Sprawdź nazwę
        if (cpRedCustomWeapon.getName() != null) {
            // Sprawdzenie, czy w danej grze jest już broń o tej nazwie już istnieje w tej grze
            if (cpRedCustomWeaponsRepository.existsByNameAndGameId(cpRedCustomWeapon.getName(), game)) {
                throw new IllegalStateException("Customowa broń o tej nazwie już istnieje w tej grze.");
            }

            // Sprawdza, czy nazwa broni nie jest pusta lub składa się tylko z białych znaków
            if (cpRedCustomWeapon.getName().trim().isEmpty() || cpRedCustomWeapon.getName().isEmpty()) {
                throw new IllegalStateException("Nazwa customowej broni nie może być pusta.");
            }

            // Sprawdź długość nazwy broni
            if (cpRedCustomWeapon.getName().length() > 255) {
                throw new IllegalStateException("Nazwa customowej broni nie może być dłuższa niż 255 znaków");
            }

            weaponToUpdate.setName(cpRedCustomWeapon.getName());
        }

        // Sprawdź typ
        if (cpRedCustomWeapon.getType() != null) {
            weaponToUpdate.setType(CpRedWeaponsType.valueOf(cpRedCustomWeapon.getType()));
        }

        // Sprawdź dmg
        if (cpRedCustomWeapon.getDamage() != null) {
            // Sprawdź, czy dmg nie jest ujemne
            if (cpRedCustomWeapon.getDamage() < 0) {
                throw new IllegalStateException("Dmg nie mogą być ujemne.");
            }
            weaponToUpdate.setDamage(cpRedCustomWeapon.getDamage());
        }

        // Sprawdź pojemność magazynka
        if (cpRedCustomWeapon.getMagazineCapacity() != null) {
            // Sprawdź, czy pojemność magazynka nie jest ujemna
            if (cpRedCustomWeapon.getMagazineCapacity() < 0) {
                throw new IllegalStateException("Pojemność magazynka nie może być ujemna.");
            }
            weaponToUpdate.setMagazineCapacity(cpRedCustomWeapon.getMagazineCapacity());
        }

        // Sprawdź ilość ataków
        if (cpRedCustomWeapon.getNumberOfAttacks() != null) {
            // Sprawdź, czy liczba ataków jest większa od 0
            if (cpRedCustomWeapon.getNumberOfAttacks() < 0) {
                throw new IllegalStateException("Liczba ataków nie może być ujemna.");
            }
            weaponToUpdate.setNumberOfAttacks(cpRedCustomWeapon.getNumberOfAttacks());
        }

        // Sprawdź ilość rąk
        if (cpRedCustomWeapon.getHandType() != null) {
            // Sprawdź, czy typ ręki jest większy od 0
            if (cpRedCustomWeapon.getHandType() < 0) {
                throw new IllegalStateException("Typ ręki nie może być ujemny.");
            }
            weaponToUpdate.setHandType(cpRedCustomWeapon.getHandType());
        }

        if (cpRedCustomWeapon.getIsHidden() != null) {
            weaponToUpdate.setIsHidden(cpRedCustomWeapon.getIsHidden());
        }

        // Sprawdź cenę
        if (cpRedCustomWeapon.getPrice() != null) {
            // Sprawdź, czy cena nie jest ujemna
            if (cpRedCustomWeapon.getPrice() < 0) {
                throw new IllegalStateException("Cena nie może być ujemna.");
            }
            weaponToUpdate.setPrice(cpRedCustomWeapon.getPrice());
        }

        // Sprawdź jakość
        if (cpRedCustomWeapon.getQuality() != null) {
            weaponToUpdate.setQuality(cpRedCustomWeapon.getQuality());
        }

        // Sprawdź dostępność
        if (cpRedCustomWeapon.getAvailability() != null) {
            weaponToUpdate.setAvailability(cpRedCustomWeapon.getAvailability());
        }

        // Sprawdź, czy broń jest modyfikowalna
        if (cpRedCustomWeapon.getIsModifiable() != null) {
            weaponToUpdate.setIsModifiable(cpRedCustomWeapon.getIsModifiable());
        }

        // Sprawdź ilość modyfikacji
        if (cpRedCustomWeapon.getModSlots() != null) {
            if (!weaponToUpdate.getIsModifiable()) {
                cpRedCustomWeapon.setModSlots((short) 0);
            } else {
                // Sprawdź, czy ilość modyfikacji nie jest ujemna
                if (cpRedCustomWeapon.getModSlots() < 0) {
                    throw new IllegalStateException("Ilość modyfikacji nie może być ujemna.");
                }
            }
            weaponToUpdate.setModSlots(cpRedCustomWeapon.getModSlots());
        }

        // Sprawdź opis
        if (cpRedCustomWeapon.getDescription() != null) {
            // Sprawdź, czy opis nie jest pusty lub składa się tylko z białych znaków
            if (cpRedCustomWeapon.getDescription().trim().isEmpty() || cpRedCustomWeapon.getDescription().isEmpty()) {
                throw new IllegalStateException("Opis customowej broni nie może być pusty.");
            }

            // Sprawdź długość opisu
            if (cpRedCustomWeapon.getDescription().length() > 500) {
                throw new IllegalStateException("Opis customowej broni nie może być dłuższy niż 500 znaków");
            }
            weaponToUpdate.setDescription(cpRedCustomWeapon.getDescription());
        }

        // Zapisanie zmienionej customowej broni
        cpRedCustomWeaponsRepository.save(weaponToUpdate);

        return CustomReturnables.getOkResponseMap("Customowa amunicja została zmodyfikowana.");
    }

    // Pobierz wszystkie customowe bronie dla admina
    public Map<String, Object> getAllCustomWeaponsForAdmin() {
        List<CpRedCustomWeapons> allCustomWeapons = cpRedCustomWeaponsRepository.findAll();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowa broń pobrana pomyślnie");
        response.put("customWeapons", allCustomWeapons);
        return response;
    }
}

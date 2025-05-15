package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeapons;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.game.GameStatus;
import dev.goral.rpgmanager.game.gameUsers.GameUsers;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomWeaponsService {
    private final CpRedCustomWeaponsRepository cpRedCustomWeaponsRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;

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
                        cpRedCustomWeapons.isHidden(),
                        cpRedCustomWeapons.getQuality().name(),
                        cpRedCustomWeapons.getPrice(),
                        cpRedCustomWeapons.getAvailability().name(),
                        cpRedCustomWeapons.isModifiable(),
                        cpRedCustomWeapons.getDescription()
                )).toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowa broń pobrana pomyślnie");
        response.put("customWeapons", allCustomWeapons);
        return response;
    }

    // Pobierz customową broń po id
    public Map<String, Object> getCustomWeaponById(Long customWeaponId) {
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
                        cpRedCustomWeapons.isHidden(),
                        cpRedCustomWeapons.getQuality().name(),
                        cpRedCustomWeapons.getPrice(),
                        cpRedCustomWeapons.getAvailability().name(),
                        cpRedCustomWeapons.isModifiable(),
                        cpRedCustomWeapons.getDescription()
                )).orElseThrow(() -> new RuntimeException("Customowa broń o id " + customWeaponId + " nie istnieje"));

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowa broń pobrana pomyślnie");
        response.put("customWeapon", weaponsDTO);
        return response;
    }

    // Dodaj customową broń
    public Map<String, Object> addCustomWeapon(AddCustomWeaponRequest addCustomWeaponRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdź, czy wszystkie wymagane pola są wypełnione
        if (addCustomWeaponRequest.getGameId() == -1L ||
                addCustomWeaponRequest.getRequiredSkillId() == -1L ||
                addCustomWeaponRequest.getName() == null ||
                addCustomWeaponRequest.getType() == null ||
                addCustomWeaponRequest.getDamage() == -1 ||
                addCustomWeaponRequest.getMagazineCapacity() == -1 ||
                addCustomWeaponRequest.getNumberOfAttacks() == -1 ||
                addCustomWeaponRequest.getHandType() == -1 ||
                addCustomWeaponRequest.getQuality() == null ||
                addCustomWeaponRequest.getPrice() == -1 ||
                addCustomWeaponRequest.getAvailability() == null ||
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
                .orElseThrow(() -> new ResourceNotFoundException("Nie jesteś graczem wybranej gry."));

        // Sprawdź, czy użytkownik jest GM

        // Sprawdź, czy umiejętność o podanym id istnieje

        // Sprawdzenie, czy w danej grze jest już broń o tej nazwie

        // Sprawdza, czy nazwa broni nie jest pusta lub składa się tylko z białych znaków

        // Sprawdź długość nazwy broni

        // Sprawdź, czy dmg nie jest ujemne

        // Sprawdź, czy pojemność magazynka nie jest ujemna

        // Sprawdź, czy liczba ataków jest większa od 0

        // Sprawdź, czy typ ręki jest większy od 0

        // Sprawdź, czy cena nie jest ujemna

        // Sprawdź, czy opis nie jest pusty lub składa się tylko z białych znaków

        // Sprawdź długość opisu

        // Zapisanie broni
        CpRedCustomWeapons newCustomWeapon = new CpRedCustomWeapons(
//                null,
//                cpRedCustomWeapons.getGameId(),
//                cpRedCustomWeapons.getRequiredSkillId(),
//                cpRedCustomWeapons.getName(),
//                cpRedCustomWeapons.getType(),
//                cpRedCustomWeapons.getDamage(),
//                cpRedCustomWeapons.getMagazineCapacity(),
//                cpRedCustomWeapons.getNumberOfAttacks(),
//                cpRedCustomWeapons.getHandType(),
//                cpRedCustomWeapons.isHidden(),
//                cpRedCustomWeapons.getQuality(),
//                cpRedCustomWeapons.getPrice(),
//                cpRedCustomWeapons.getAvailability(),
//                cpRedCustomWeapons.isModifiable(),
//                cpRedCustomWeapons.getDescription()
        );

        cpRedCustomWeaponsRepository.save(newCustomWeapon);
        return CustomReturnables.getOkResponseMap("Customowa amunicja została dodana.");
    }

    // Modyfikuj customową broń
    public Map<String, Object> updateCustomWeapon(Long customWeaponId, CpRedCustomWeapons cpRedCustomWeapons) {


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

package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeaponMods;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameRepository;
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
public class CpRedCustomWeaponModsService {
    private final CpRedCustomWeaponModsRepository cpRedCustomWeaponModsRepository;
    private final GameUsersRepository gameUsersRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;



    // Pobierz wszystkie modyfikacje broni
    public Map<String, Object> getAllWeaponMods() {
        List<CpRedCustomWeaponModsDTO> allCustomWeaponMods = cpRedCustomWeaponModsRepository.findAll().stream()
                .map(CpRedCustomWeaponMods -> new CpRedCustomWeaponModsDTO(
                        CpRedCustomWeaponMods.getGameId().getId(),
                        CpRedCustomWeaponMods.getName(),
                        CpRedCustomWeaponMods.getPrice(),
                        CpRedCustomWeaponMods.getSize(),
                        CpRedCustomWeaponMods.getAvailability().toString(),
                        CpRedCustomWeaponMods.getDescription()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe modyfikacje broni zostały pobrane.");
        response.put("customWeaponMods", allCustomWeaponMods);
        return response;
    }


    public Map<String, Object> getWeaponModById(Long weaponModId) {
        CpRedCustomWeaponModsDTO cpRedCustomWeaponMods = cpRedCustomWeaponModsRepository.findById(weaponModId)
                .map(CpRedCustomWeaponMods -> new CpRedCustomWeaponModsDTO(
                        CpRedCustomWeaponMods.getGameId().getId(),
                        CpRedCustomWeaponMods.getName(),
                        CpRedCustomWeaponMods.getPrice(),
                        CpRedCustomWeaponMods.getSize(),
                        CpRedCustomWeaponMods.getAvailability().toString(),
                        CpRedCustomWeaponMods.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Customowa modyfikacja o id " + weaponModId + " nie istnieje."));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowa modyfikacja broni została pobrana.");
        response.put("customWeaponMod", cpRedCustomWeaponMods);
        return response;
    }

    public Map<String, Object> getWeaponModsByGame(Long gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + gameId + " nie istnieje."));

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie jesteś graczem wybranej gry."));
        List<CpRedCustomWeaponModsDTO> allCustomMods=cpRedCustomWeaponModsRepository.findAllByGameId(game).stream()
                .map(CpRedCustomWeaponMods -> new CpRedCustomWeaponModsDTO(
                        CpRedCustomWeaponMods.getGameId().getId(),
                        CpRedCustomWeaponMods.getName(),
                        CpRedCustomWeaponMods.getPrice(),
                        CpRedCustomWeaponMods.getSize(),
                        CpRedCustomWeaponMods.getAvailability().toString(),
                        CpRedCustomWeaponMods.getDescription()
                )).toList();
        Map<String,Object> response= CustomReturnables.getOkResponseMap("Customowe modyfikacje do broni, do gry zostały pobrane.");
        response.put("customMods",allCustomMods);
        return response;
    }



//
//    // Dodaj modyfikację broni
//    public Map<String, Object> addWeaponMod(CpRedCustomWeaponMods cpRedCustomWeaponMods) {
//
//    }
//
//    // Modyfikować modyfikację broni
//    public Map<String, Object> updateWeaponMod(Long weaponModId, CpRedCustomWeaponMods cpRedCustomWeaponMods) {
//
//    }
//
//    // Pobierz wszystkie modyfikacje broni dla admina
//    public Map<String, Object> getAllWeaponModsForAdmin() {
//
//    }
}

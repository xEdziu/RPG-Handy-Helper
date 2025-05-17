package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsers;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomArmorsService {
    private final CpRedCustomArmorsRepository cpRedCustomArmorsRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameUsersRepository gameUsersRepository;

    // Pobierz wszystkie customowe zbroje
    public Map<String, Object> getAllCustomArmors() {
        List<CpRedCustomArmorsDTO> allCustomArmorsList = cpRedCustomArmorsRepository.findAll().stream()
                .map(CpRedCustomArmors-> new CpRedCustomArmorsDTO(
                        CpRedCustomArmors.getGameId().getId(),
                        CpRedCustomArmors.getName(),
                        CpRedCustomArmors.getType().toString(),
                        CpRedCustomArmors.getArmorPoints(),
                        CpRedCustomArmors.getPenalty(),
                        CpRedCustomArmors.getPrice(),
                        CpRedCustomArmors.getAvailability().toString(),
                        CpRedCustomArmors.getDescription()
                ))
                .toList();
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Customowe pancerze zostały pobrane.");
        response.put("customArmors", allCustomArmorsList);
        return response;
    }
//
    // Pobierz customową zbroję po id
    public Map<String, Object> getCustomArmorById(Long armorId) {
        CpRedCustomArmorsDTO customArmor = cpRedCustomArmorsRepository.findById(armorId)
                .map(CpRedCustomArmors-> new CpRedCustomArmorsDTO(
                        CpRedCustomArmors.getGameId().getId(),
                        CpRedCustomArmors.getName(),
                        CpRedCustomArmors.getType().toString(),
                        CpRedCustomArmors.getArmorPoints(),
                        CpRedCustomArmors.getPenalty(),
                        CpRedCustomArmors.getPrice(),
                        CpRedCustomArmors.getAvailability().toString(),
                        CpRedCustomArmors.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Customowy pancerz o id " + armorId + " nie istnieje."));
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Customowa zbroja została pobrana.");
        response.put("customArmor", customArmor);
        return response;
    }
    public Map<String, Object> getCustomArmorByGame(Long gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + gameId + " nie istnieje."));

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie jesteś graczem wybranej gry."));
        List<CpRedCustomArmorsDTO> gameCustomArmorsList = cpRedCustomArmorsRepository.findAllByGameId(gameId).stream()
                .map(CpRedCustomArmors-> new CpRedCustomArmorsDTO(
                        CpRedCustomArmors.getGameId().getId(),
                        CpRedCustomArmors.getName(),
                        CpRedCustomArmors.getType().toString(),
                        CpRedCustomArmors.getArmorPoints(),
                        CpRedCustomArmors.getPenalty(),
                        CpRedCustomArmors.getPrice(),
                        CpRedCustomArmors.getAvailability().toString(),
                        CpRedCustomArmors.getDescription()
                )).toList();

        Map<String,Object> response = CustomReturnables.getOkResponseMap("Customowe zbroje do gry zostały pobrane.");
        response.put("customArmor", gameCustomArmorsList);
        return response;
    }


    // Dodaj customową zbroję
//    public Map<String, Object> addCustomArmor(CpRedCustomArmorsRequest cpRedCustomArmors) {
//
//
//    }
//
//    // Modyfikuj customową zbroję
//    public Map<String, Object> updateCustomArmor(Long armorId, CpRedCustomArmors cpRedCustomArmors) {
//
//    }
//
    // Pobierz wszystkie customowe zbroje dla admina
    public Map<String, Object> getAllCustomArmorsForAdmin() {
        List<CpRedCustomArmors> allCustomArmorsList = cpRedCustomArmorsRepository.findAll();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe pancerze zostały pobrane dla administratora.");
        response.put("customArmors", allCustomArmorsList);
        return response;
    }
}

package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customCriticalInjuries;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsers;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors.CpRedCustomArmorsDTO;
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
public class CpRedCustomCriticalInjuriesService {
    private final CpRedCustomCriticalInjuriesRepository cpRedCustomCriticalInjuriesRepository;
    private final UserRepository userRepository;
    private final GameUsersRepository gameUsersRepository;
    private final GameRepository gameRepository;

    public Map<String, Object> getAllCustomCriticalInjuries() {
        List<CpRedCustomCriticalInjuriesDTO> allCustomCriticalInjuries = cpRedCustomCriticalInjuriesRepository.findAll().stream()
                .map(CpRedCustomCriticalInjuries -> new CpRedCustomCriticalInjuriesDTO(
                        CpRedCustomCriticalInjuries.getGameId().getId(),
                        CpRedCustomCriticalInjuries.getInjuryPlace().toString(),
                        CpRedCustomCriticalInjuries.getName(),
                        CpRedCustomCriticalInjuries.getEffects(),
                        CpRedCustomCriticalInjuries.getPatching(),
                        CpRedCustomCriticalInjuries.getTreating()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe rany krytyczne zostały pobrane");
        response.put("customCriticalInjuries", allCustomCriticalInjuries);
        return response;

    }

    // Pobierz customową ranę krytyczną po id
    public Map<String, Object> getCustomCriticalInjuryById(Long customCriticalInjuryId) {
        CpRedCustomCriticalInjuriesDTO customCriticalInjuries = cpRedCustomCriticalInjuriesRepository.findById(customCriticalInjuryId)
                .map(CpRedCustomCriticalInjuries -> new CpRedCustomCriticalInjuriesDTO(
                        CpRedCustomCriticalInjuries.getGameId().getId(),
                        CpRedCustomCriticalInjuries.getInjuryPlace().toString(),
                        CpRedCustomCriticalInjuries.getName(),
                        CpRedCustomCriticalInjuries.getEffects(),
                        CpRedCustomCriticalInjuries.getPatching(),
                        CpRedCustomCriticalInjuries.getTreating()
                )).orElseThrow(() -> new ResourceNotFoundException("Customowe rany krytyczne o id " + customCriticalInjuryId + " nie istnieją."));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowa rana krytyczna została pobrana");
        response.put("customCriticalInjuries", customCriticalInjuries);
        return response;
    }

    public Map<String, Object> getCustomCriticalInjuryByGame(Long gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + gameId + " nie istnieje."));

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie jesteś graczem wybranej gry."));
        List<CpRedCustomCriticalInjuriesDTO> customCriticalInjuries = cpRedCustomCriticalInjuriesRepository.findAllByGameId(game).stream()
                .map(CpRedCustomCriticalInjuries -> new CpRedCustomCriticalInjuriesDTO(
                        CpRedCustomCriticalInjuries.getGameId().getId(),
                        CpRedCustomCriticalInjuries.getInjuryPlace().toString(),
                        CpRedCustomCriticalInjuries.getName(),
                        CpRedCustomCriticalInjuries.getEffects(),
                        CpRedCustomCriticalInjuries.getPatching(),
                        CpRedCustomCriticalInjuries.getTreating()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe rany krytyczne do gry zostały pobrane");
        response.put("customCriticalInjuries", customCriticalInjuries);
        return response;
    }

//
//    // Dodać customową ranę krytyczną
//    public Map<String, Object> addCustomCriticalInjury(CpRedCustomCriticalInjuries cpRedCustomCriticalInjuries) {
//
//    }
//
//    // Modyfikować customową ranę krytyczną
//    public Map<String, Object> updateCustomCriticalInjury(Long customCriticalInjuryId, CpRedCustomCriticalInjuries cpRedCustomCriticalInjuries) {
//
//    }
//
//    // Pobierz wszystkie customowe rany krytyczne dla admina
//    public Map<String, Object> getAllCustomCriticalInjuriesForAdmin() {
//
//    }

}

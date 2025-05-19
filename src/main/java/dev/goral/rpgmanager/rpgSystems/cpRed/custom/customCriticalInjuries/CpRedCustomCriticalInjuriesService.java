package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customCriticalInjuries;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.game.GameStatus;
import dev.goral.rpgmanager.game.gameUsers.GameUsers;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRole;
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


    public Map<String, Object> addCustomCriticalInjury(CpRedCustomCriticalInjuriesRequest cpRedCustomCriticalInjuries) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if(cpRedCustomCriticalInjuries.getGameId() == null||
            cpRedCustomCriticalInjuries.getInjuryPlace()==null||
            cpRedCustomCriticalInjuries.getName()==null||
            cpRedCustomCriticalInjuries.getEffects()==null||
            cpRedCustomCriticalInjuries.getPatching()==null||
            cpRedCustomCriticalInjuries.getTreating()==null) {
            throw new IllegalStateException("Wszystkie pola muszą być wypełnione.");
        }
        Game game = gameRepository.findById(cpRedCustomCriticalInjuries.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + cpRedCustomCriticalInjuries.getGameId() + " nie istnieje."));
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + cpRedCustomCriticalInjuries.getGameId() + " nie jest aktywna.");
        }
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), cpRedCustomCriticalInjuries.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może dodać pancerz do gry.");
        }
        if (cpRedCustomCriticalInjuriesRepository.existsByNameAndGameId(cpRedCustomCriticalInjuries.getName(), game)) {
            throw new IllegalStateException("Customowe rany krytyczne o tej nazwie już istnieje w tej grze.");
        }
        if (cpRedCustomCriticalInjuries.getName().isEmpty() ||
                cpRedCustomCriticalInjuries.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa ran krytycznych nie może być pusta.");
        }
        if (cpRedCustomCriticalInjuries.getName().length() > 255) {
            throw new IllegalStateException("Nazwa ran krytycznych nie może być dłuższa niż 255 znaków.");
        }
        if (cpRedCustomCriticalInjuries.getEffects().isEmpty() ||
                cpRedCustomCriticalInjuries.getEffects().trim().isEmpty()) {
            throw new IllegalStateException("Efekty ran krytycznych nie mogą być puste.");
        }
        if (cpRedCustomCriticalInjuries.getEffects().length() > 500) {
            throw new IllegalStateException("Efekty ran krytycznych nie mogą być dłuższe niż 500 znaków.");
        }
        if (cpRedCustomCriticalInjuries.getPatching().isEmpty() ||
                cpRedCustomCriticalInjuries.getPatching().trim().isEmpty()) {
            throw new IllegalStateException("Łatanie ran krytycznych nie może być puste.");
        }
        if (cpRedCustomCriticalInjuries.getPatching().length() > 255) {
            throw new IllegalStateException("Łatanie ran krytycznych nie może być dłuższe niż 255 znaków.");
        }
        if (cpRedCustomCriticalInjuries.getTreating().isEmpty() ||
                cpRedCustomCriticalInjuries.getTreating().trim().isEmpty()) {
            throw new IllegalStateException("Leczenie ran krytycznych nie może być puste.");
        }
        if (cpRedCustomCriticalInjuries.getTreating().length() > 255) {
            throw new IllegalStateException("Leczenie ran krytycznych nie może być dłuższe niż 255 znaków.");
        }
        CpRedCustomCriticalInjuries newCustomCriticalInjuries = new CpRedCustomCriticalInjuries(
                null,
                game,
                cpRedCustomCriticalInjuries.getInjuryPlace(),
                cpRedCustomCriticalInjuries.getName(),
                cpRedCustomCriticalInjuries.getEffects(),
                cpRedCustomCriticalInjuries.getPatching(),
                cpRedCustomCriticalInjuries.getTreating()
        );
        cpRedCustomCriticalInjuriesRepository.save(newCustomCriticalInjuries);
        return CustomReturnables.getOkResponseMap("Customowe rany krytyczne zostały dodane.");
    }
//
//    // Modyfikować customową ranę krytyczną
//    public Map<String, Object> updateCustomCriticalInjury(Long customCriticalInjuryId, CpRedCustomCriticalInjuries cpRedCustomCriticalInjuries) {
//
//    }
//
    public Map<String, Object> getAllCustomCriticalInjuriesForAdmin() {
    List<CpRedCustomCriticalInjuries> allCustomCriticalInjuries = cpRedCustomCriticalInjuriesRepository.findAll();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe rany krytyczne zostały pobrane dla administratora");
        response.put("customCriticalInjuries", allCustomCriticalInjuries);
        return response;
    }

}

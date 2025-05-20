package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customCyberwares;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsers;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRole;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors.CpRedCustomArmors;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors.CpRedCustomArmorsService;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customCriticalInjuries.CpRedCustomCriticalInjuries;
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
import java.util.Stack;

@Service
@AllArgsConstructor
public class CpRedCustomCyberwaresService {
    private final CpRedCustomCyberwaresRepository cpRedCustomCyberwaresRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameUsersRepository gameUsersRepository;
    public Map<String, Object> getAllCyberware() {
        List<CpRedCustomCyberwaresDTO> allCustomCyberwares=cpRedCustomCyberwaresRepository.findAll().stream()
                .map(CpRedCustomCyberwares -> new CpRedCustomCyberwaresDTO(
                        CpRedCustomCyberwares.getGameId().getId(),
                        CpRedCustomCyberwares.getName(),
                        CpRedCustomCyberwares.getMountPlace().toString(),
                        CpRedCustomCyberwares.getRequirements(),
                        CpRedCustomCyberwares.getHumanityLoss(),
                        CpRedCustomCyberwares.getSize(),
                        CpRedCustomCyberwares.getInstallationPlace().toString(),
                        CpRedCustomCyberwares.getPrice(),
                        CpRedCustomCyberwares.getAvailability().toString(),
                        CpRedCustomCyberwares.getDescription()
                )).toList();
        Map<String,Object> response= CustomReturnables.getOkResponseMap("Customowe wszczepy zostały pobrane.");
        response.put("customCyberwares",allCustomCyberwares);
        return response;
    }


    public Map<String, Object> getCyberwareById(Long cyberwareId) {
        CpRedCustomCyberwaresDTO customCyberware = cpRedCustomCyberwaresRepository.findById(cyberwareId)
                .map(CpRedCustomCyberwares -> new CpRedCustomCyberwaresDTO(
                        CpRedCustomCyberwares.getGameId().getId(),
                        CpRedCustomCyberwares.getName(),
                        CpRedCustomCyberwares.getMountPlace().toString(),
                        CpRedCustomCyberwares.getRequirements(),
                        CpRedCustomCyberwares.getHumanityLoss(),
                        CpRedCustomCyberwares.getSize(),
                        CpRedCustomCyberwares.getInstallationPlace().toString(),
                        CpRedCustomCyberwares.getPrice(),
                        CpRedCustomCyberwares.getAvailability().toString(),
                        CpRedCustomCyberwares.getDescription()
                )).orElseThrow(()-> new ResourceNotFoundException("Customowy wszczep o id " + cyberwareId + " nie istnieje."));
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Customowy wszczep został pobran.");
        response.put("customCyberware", customCyberware);
        return response;

    }
    public Map<String, Object> getCyberwareByGame(Long gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + gameId + " nie istnieje."));

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie jesteś graczem wybranej gry."));
        List<CpRedCustomCyberwaresDTO> allCustomCyberwares=cpRedCustomCyberwaresRepository.findAllByGameId(game).stream()
                .map(CpRedCustomCyberwares -> new CpRedCustomCyberwaresDTO(
                        CpRedCustomCyberwares.getGameId().getId(),
                        CpRedCustomCyberwares.getName(),
                        CpRedCustomCyberwares.getMountPlace().toString(),
                        CpRedCustomCyberwares.getRequirements(),
                        CpRedCustomCyberwares.getHumanityLoss(),
                        CpRedCustomCyberwares.getSize(),
                        CpRedCustomCyberwares.getInstallationPlace().toString(),
                        CpRedCustomCyberwares.getPrice(),
                        CpRedCustomCyberwares.getAvailability().toString(),
                        CpRedCustomCyberwares.getDescription()
                )).toList();
        Map<String,Object> response= CustomReturnables.getOkResponseMap("Customowe wszczepy do gry zostały pobrane.");
        response.put("customCyberwares",allCustomCyberwares);
        return response;
    }

    public Map<String, Object> addCyberware(CpRedCustomCyberwaresRequest cpRedCustomCyberwares) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if (cpRedCustomCyberwares.getGameId()==null||
                cpRedCustomCyberwares.getName()==null||
                cpRedCustomCyberwares.getMountPlace()==null||
                cpRedCustomCyberwares.getRequirements()==null||
                cpRedCustomCyberwares.getHumanityLoss()==null||
                cpRedCustomCyberwares.getSize()<0||
                cpRedCustomCyberwares.getInstallationPlace()==null||
                cpRedCustomCyberwares.getPrice()<0||
                cpRedCustomCyberwares.getAvailability()==null||
                cpRedCustomCyberwares.getDescription()==null) {
            throw new IllegalStateException("Wszystkie pola muszą być wypełnione.");
        }
        Game game = gameRepository.findById(cpRedCustomCyberwares.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + cpRedCustomCyberwares.getGameId() + " nie istnieje."));
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), cpRedCustomCyberwares.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));

        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może dodać pancerz do gry.");
        }
        if (cpRedCustomCyberwaresRepository.existsByNameAndGameId(cpRedCustomCyberwares.getName(), game)) {
            throw new IllegalStateException("Customowy szczep o tej nazwie już istnieje w tej grze.");
        }
        if (cpRedCustomCyberwares.getName().isEmpty() ||
                cpRedCustomCyberwares.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa wszczepu nie może być pusta.");
        }
        if (cpRedCustomCyberwares.getName().length() > 255) {
            throw new IllegalStateException("Nazwa wszczepu nie może być dłuższa niż 255 znaków.");
        }
        if (cpRedCustomCyberwares.getRequirements().isEmpty() ||
                cpRedCustomCyberwares.getRequirements().trim().isEmpty()) {
            throw new IllegalStateException("Wymagania wszczepu nie mogą być puste.");
        }
        if (cpRedCustomCyberwares.getRequirements().length() > 500) {
            throw new IllegalStateException("Wymagania wszczepu nie mogą być dłuższe niż 500 znaków.");
        }
        if (cpRedCustomCyberwares.getHumanityLoss().isEmpty() ||
                cpRedCustomCyberwares.getHumanityLoss().trim().isEmpty()) {
            throw new IllegalStateException("Utrata ludzkosci wszczepu nie może być pusta.");
        }
        if (cpRedCustomCyberwares.getHumanityLoss().length() > 255) {
            throw new IllegalStateException("Utrata ludzkosci wszczepu nie może być dłuższa niż 255 znaków.");
        }
        if (cpRedCustomCyberwares.getDescription().isEmpty() ||
                cpRedCustomCyberwares.getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Opis wszczepu nie może być pusty.");
        }
        if (cpRedCustomCyberwares.getDescription().length() > 500) {
            throw new IllegalStateException("Opis wszczepu nie może być dłuższy niż 500 znaków.");
        }
        if (cpRedCustomCyberwares.getSize() < 0) {
            throw new IllegalStateException("Rozmiar wszczepu nie może być mniejszy lub równy 0.");
        }
        if (cpRedCustomCyberwares.getPrice() < 0) {
            throw new IllegalStateException("Cena wszczepu nie może być mniejsza lub równa 0.");
        }
        CpRedCustomCyberwares newCpRedCustomCyberwares = new CpRedCustomCyberwares(
                null,
                game,
                cpRedCustomCyberwares.getName(),
                cpRedCustomCyberwares.getMountPlace(),
                cpRedCustomCyberwares.getRequirements(),
                cpRedCustomCyberwares.getHumanityLoss(),
                cpRedCustomCyberwares.getSize(),
                cpRedCustomCyberwares.getInstallationPlace(),
                cpRedCustomCyberwares.getPrice(),
                cpRedCustomCyberwares.getAvailability(),
                cpRedCustomCyberwares.getDescription()
        );
        cpRedCustomCyberwaresRepository.save(newCpRedCustomCyberwares);
        return CustomReturnables.getOkResponseMap("Customowy wszczep został dodany.");
    }
//
//    // Modyfikować cyberware
//    public Map<String, Object> updateCyberware(Long cyberwareId, CpRedCustomCyberwares cpRedCustomCyberwares) {
//
//    }
//
    // Pobierz wszystkie cyberware dla admina
    public Map<String, Object> getAllCyberwareForAdmin() {
        List<CpRedCustomCyberwares> allCustomCyberwaresList = cpRedCustomCyberwaresRepository.findAll();
        Map<String,Object> response= CustomReturnables.getOkResponseMap("Customowe wszczepy zostały pobrane dla administratora.");
        response.put("customCyberwares",allCustomCyberwaresList);
        return response;
    }
}

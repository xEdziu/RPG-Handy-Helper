package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customCyberwares;

import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors.CpRedCustomArmors;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors.CpRedCustomArmorsService;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//
//    // Pobierz cyberware po id
//    public Map<String, Object> getCyberwareById(Long cyberwareId) {
//
//    }
//
//    // Doadaj cyberware
//    public Map<String, Object> addCyberware(CpRedCustomCyberwares cpRedCustomCyberwares) {
//
//    }
//
//    // Modyfikować cyberware
//    public Map<String, Object> updateCyberware(Long cyberwareId, CpRedCustomCyberwares cpRedCustomCyberwares) {
//
//    }
//
//    // Pobierz wszystkie cyberware dla admina
//    public Map<String, Object> getAllCyberwareForAdmin() {
//
//    }
}

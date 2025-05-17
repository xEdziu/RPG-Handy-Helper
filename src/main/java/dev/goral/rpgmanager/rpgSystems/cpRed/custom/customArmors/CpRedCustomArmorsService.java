package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors;

import dev.goral.rpgmanager.security.CustomReturnables;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomArmorsService {
    private final CpRedCustomArmorsRepository cpRedCustomArmorsRepository;

    // Pobierz wszystkie customowe zbroje
    public Map<String, Object> getAllCustomArmors() {
        List<CpRedCustomArmorsDTO> allCustomArmorsList = cpRedCustomArmorsRepository.findAll().stream()
                .map(CpRedCustomArmors-> new CpRedCustomArmorsDTO(
                        CpRedCustomArmors.getGameId().getId(),
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
//    // Pobierz customową zbroję po id
//    public Map<String, Object> getCustomArmorById(Long armorId) {
//
//    }
//
//    // Dodaj customową zbroję
//    public Map<String, Object> addCustomArmor(CpRedCustomArmors cpRedCustomArmors) {
//
//    }
//
//    // Modyfikuj customową zbroję
//    public Map<String, Object> updateCustomArmor(Long armorId, CpRedCustomArmors cpRedCustomArmors) {
//
//    }
//
//    // Pobierz wszystkie customowe zbroje dla admina
//    public Map<String, Object> getAllCustomArmorsForAdmin() {
//
//    }
}

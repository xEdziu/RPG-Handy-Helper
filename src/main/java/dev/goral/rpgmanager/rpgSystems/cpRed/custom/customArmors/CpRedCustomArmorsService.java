package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomArmorsService {
    private final CpRedCustomArmorsRepository cpRedCustomArmorsRepository;

    // Pobierz wszystkie customowe zbroje
    public List<CpRedCustomArmorsDTO> getAllCustomArmors() {

    }

    // Pobierz customową zbroję po id
    public CpRedCustomArmorsDTO getCustomArmorById(Long armorId) {

    }

    // Dodaj customową zbroję
    public Map<String, Object> addCustomArmor(CpRedCustomArmors cpRedCustomArmors) {

    }

    // Modyfikuj customową zbroję
    public Map<String, Object> updateCustomArmor(Long armorId, CpRedCustomArmors cpRedCustomArmors) {

    }

    // Pobierz wszystkie customowe zbroje dla admina
    public List<CpRedCustomArmors> getAllCustomArmorsForAdmin() {
        return cpRedCustomArmorsRepository.findAll();
    }
}

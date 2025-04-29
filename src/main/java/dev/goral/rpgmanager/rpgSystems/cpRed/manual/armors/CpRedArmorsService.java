package dev.goral.rpgmanager.rpgSystems.cpRed.manual.armors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedArmorsService {
    private final CpRedArmorsRepository cpRedArmorsRepository;

    // Pobierz wszystkie pancerze
    public List<CpRedArmorsDTO> getAllArmors() {

    }

    // Pobierz pancerz po id
    public CpRedArmorsDTO getArmorById(Long armorId) {

    }

    // Pobierz wszystkie pancerze dla admina
    public List<CpRedArmors> getAllArmorsForAdmin() {
        return cpRedArmorsRepository.findAll();
    }

    // Dodać pancerz
    public Map<String, Object> addArmor(CpRedArmors cpRedArmors) {

    }

    // Modyfikować pancerz
    public Map<String, Object> updateArmor(Long armorId, CpRedArmors cpRedArmors) {

    }

}

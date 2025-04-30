package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customAmmunition;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomAmmunitionService {
    private final CpRedCustomAmmunitionRepository cpRedCustomAmmunitionRepository;

    // Pobierz wszystkie amunicje
    public List<CpRedCustomAmmunitionDTO> getAllCustomAmmunition() {

    }

    // Pobierz amunicje po id
    public CpRedCustomAmmunitionDTO getCustomAmmunitionById(Long ammunitionId) {

    }

    // Dodaj amunicje
    public Map<String, Object> addCustomAmmunition(CpRedCustomAmmunition cpRedCustomAmmunition) {

    }

    // Modyfikuj amunicje
    public Map<String, Object> updateCustomAmmunition(Long ammunitionId,
                                                           CpRedCustomAmmunition cpRedCustomAmmunition) {

    }

    // Pobierz wszystkie amunicje dla admina
    public List<CpRedCustomAmmunition> getAllCustomAmmunitionForAdmin() {
        return cpRedCustomAmmunitionRepository.findAll();
    }
}

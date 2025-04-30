package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customAmmunition;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public CpRedCustomAmmunitionDTO addCustomAmmunition(CpRedCustomAmmunition cpRedCustomAmmunition) {

    }

    // Modyfikuj amunicje
    public CpRedCustomAmmunitionDTO updateCustomAmmunition(Long ammunitionId,
                                                           CpRedCustomAmmunition cpRedCustomAmmunition) {

    }

    // Pobierz wszystkie amunicje dla admina
    public List<CpRedCustomAmmunition> getAllCustomAmmunitionForAdmin() {
        return cpRedCustomAmmunitionRepository.findAll();
    }
}

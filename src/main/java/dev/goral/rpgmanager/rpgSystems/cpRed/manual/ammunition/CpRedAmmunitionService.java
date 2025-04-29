package dev.goral.rpgmanager.rpgSystems.cpRed.manual.ammunition;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedAmmunitionService {
    private final CpRedAmmunitionRepository cpRedAmmunitionRepository;

    // Pobierz wszystkie amunicje
    public List<CpRedAmmunitionDTO> getAllAmmunition(){

    }

    // Pobierz amunicje po id
    public CpRedAmmunitionDTO getAmmunitionById(Long ammunitionId){

    }

    // Pobierz wszystkie amunicje dla admina
    public List<CpRedAmmunition> getAllAmmunitionForAdmin(){
        return cpRedAmmunitionRepository.findAll();
    }

    // Dodaj amunicje
    public Map<String, Object> addAmmunition(CpRedAmmunition cpRedAmmunition){

    }

    // Modyfikuj amunicje
    public Map<String, Object> updateAmmunition(Long ammunitionId, CpRedAmmunition cpRedAmmunition){

    }
}

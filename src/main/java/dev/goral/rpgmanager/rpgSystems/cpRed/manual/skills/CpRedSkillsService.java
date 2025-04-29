package dev.goral.rpgmanager.rpgSystems.cpRed.manual.skills;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedSkillsService {
    private final CpRedSkillsRepository cpRedSkillsRepository;

    // Pobierz wszystkie umiejętności
    public List<CpRedSkillsDTO> getAllSkills(){

    }

    // Pobierz umiejętność po id
    public CpRedSkillsDTO getSkillById(Long id) {

    }

    // Pobierz wszystkie umiejętności dla admina
    public List<CpRedSkills> getAllSkillsForAdmin() {
        return cpRedSkillsRepository.findAll();
    }

    // Dodaj umiejętność
    public Map<String, Object> addSkill(CpRedSkills cpRedSkills) {

    }

    // Modyfikuj umiejętność
    public Map<String, Object> updateSkill(Long id, CpRedSkills cpRedSkills) {

    }

}

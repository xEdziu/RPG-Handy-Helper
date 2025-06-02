package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.skills;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedSkillsController {
    private CpRedSkillsService cpRedSkillsService;

    // ============ User methods ============
    // Pobierz wszystkie umiejętności
    @GetMapping(path = "/rpgSystems/cpRed/skills/all")
    public Map<String, Object> getAllSkills() { // List<CpRedSkillsDTO>
        return cpRedSkillsService.getAllSkills();
    }
    // Pobierz umiejętność po id
    @GetMapping(path = "/rpgSystems/cpRed/skills/{skillId}")
    public Map<String, Object> getSkillById(@PathVariable("skillId") Long skillId) { // CpRedSkillsDTO
        return cpRedSkillsService.getSkillById(skillId);
    }

    // ============ Admin methods ============
    // Pobierz wszystkie umiejętności dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/skills/all")
    public Map<String, Object> getAllSkillsForAdmin() { // List<CpRedSkills>
        return cpRedSkillsService.getAllSkillsForAdmin();
    }
    // Dodać umiejętność
    @PostMapping(path = "/admin/rpgSystems/cpRed/skills/add")
    public Map<String, Object> addSkill(@RequestBody AddSkillRequest cpRedSkills) {
        return cpRedSkillsService.addSkill(cpRedSkills);
    }
    // Modyfikować umiejętność
    @PutMapping(path = "/admin/rpgSystems/cpRed/skills/update/{skillId}")
    public Map<String, Object> updateSkill(@PathVariable("skillId") Long skillId,
                                           @RequestBody AddSkillRequest cpRedSkills) {
        return cpRedSkillsService.updateSkill(skillId, cpRedSkills);
    }
}

package dev.goral.rpgmanager.rpgSystems.cpRed.manual.skills;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedSkillsController {
    private CpRedSkillsService cpRedSkillsService;

//    // ============ User methods ============
//    // Pobierz wszystkie umiejętności
//    @GetMapping(path = "/rpgSystems/cpRed/skills/all")
//    public List<CpRedSkillsDTO> getAllSkills() {
//        return cpRedSkillsService.getAllSkills();
//    }
//    // Pobierz umiejętność po id
//    @GetMapping(path = "/rpgSystems/cpRed/skills/{skillId}")
//    public CpRedSkillsDTO getSkillById(@PathVariable("skillId") Long skillId) {
//        return cpRedSkillsService.getSkillById(skillId);
//    }
//
//    // ============ Admin methods ============
//    // Pobierz wszystkie umiejętności dla admina
//    @GetMapping(path = "/admin/rpgSystems/cpRed/skills/all")
//    public List<CpRedSkills> getAllSkillsForAdmin() {
//        return cpRedSkillsService.getAllSkillsForAdmin();
//    }
//    // Dodać umiejętność
//    @PostMapping(path = "/admin/rpgSystems/cpRed/skills/add")
//    public Map<String, Object> addSkill(@RequestBody CpRedSkills cpRedSkills) {
//        return cpRedSkillsService.addSkill(cpRedSkills);
//    }
//    // Modyfikować umiejętność
//    @PutMapping(path = "/admin/rpgSystems/cpRed/skills/update/{skillId}")
//    public Map<String, Object> updateSkill(@PathVariable("skillId") Long skillId,
//                                           @RequestBody CpRedSkills cpRedSkills) {
//        return cpRedSkillsService.updateSkill(skillId, cpRedSkills);
//    }
}

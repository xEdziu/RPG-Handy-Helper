package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterSkills;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterSkillsController {
    private final CpRedCharacterSkillsService cpRedCharacterSkillsService;

    // ============ User methods ============
    @GetMapping(path = "/games/cpRed/characters/skills/{characterId}")
    public Map<String, Object> getCharacterSkills(@PathVariable("characterId") Long characterId) {
        return cpRedCharacterSkillsService.getCharacterSkills(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/skills/create")
    public Map<String, Object> createCharacterSkill(@RequestBody AddCharacterSkillsRequest addCharacterSkillsRequest) {
        return cpRedCharacterSkillsService.createCharacterSkill(addCharacterSkillsRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/skills/update/{characterSkillId}")
    public Map<String, Object> updateCharacterSkill(@PathVariable("characterSkillId") Long characterSkillId, @RequestBody UpdateCharacterSkillsRequest updateCharacterSkillsRequest) {
        return cpRedCharacterSkillsService.updateCharacterSkill(characterSkillId, updateCharacterSkillsRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/skills/delete/{characterSkillId}")
    public Map<String, Object> deleteCharacterSkill(@PathVariable("characterSkillId") Long characterSkillId) {
        return cpRedCharacterSkillsService.deleteCharacterSkill(characterSkillId);
    }
    // ============ Admin methods ============
    @GetMapping(path = "/admin/games/cpRed/characters/skills/all")
    public Map<String, Object> getAllCharactersSkills() {
        return cpRedCharacterSkillsService.getAllCharactersSkills();
    }
}

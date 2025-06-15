package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries.CpRedCharacterCriticalInjuriesRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterCriticalInjuriesController {
    private final CpRedCharacterCriticalInjuriesService cpRedCharacterCriticalInjuriesService;

    @GetMapping(path = "/rpgSystems/cpRed/character/criticalInjuries/all")
    public Map<String, Object> getAllCriticalInjuries() {
        return cpRedCharacterCriticalInjuriesService.getAllCriticalInjuries();
    }

    @GetMapping(path = "/rpgSystems/cpRed/character/criticalInjuries/{injuryId}")
    public Map<String, Object> getInjuryById(@PathVariable("injuryId") Long injuryId) {
        return cpRedCharacterCriticalInjuriesService.getCriticalInjuryById(injuryId);
    }

    @GetMapping(path = "/rpgSystems/cpRed/character/criticalInjuries/character/{characterId}")
    public Map<String, Object> getCriticalInjuriesByCharacterId(@PathVariable("characterId") Long characterId) {
        return cpRedCharacterCriticalInjuriesService.getCriticalInjuriesByCharacterId(characterId);
    }

    @GetMapping(path = "/admin/rpgSystems/cpRed/character/criticalInjuries/all")
    public Map<String, Object> getAllCriticalInjuriesForAdmin() {
        return cpRedCharacterCriticalInjuriesService.getAllCriticalInjuriesForAdmin();
    }

    @PostMapping(path = "/rpgSystems/cpRed/character/criticalInjuries/add")
    public Map<String, Object> addInjury(@RequestBody CpRedCharacterCriticalInjuriesRequest cpRedCharacterCriticalInjuries) {
        return cpRedCharacterCriticalInjuriesService.addInjury(cpRedCharacterCriticalInjuries);
    }

    @DeleteMapping(path = "/rpgSystems/cpRed/character/criticalInjuries/delete/{injuryId}")
    public Map<String, Object> deleteInjury(@PathVariable("injuryId") Long injuryId) {
        return cpRedCharacterCriticalInjuriesService.deleteInjury(injuryId);
    }

    @PutMapping(path = "/rpgSystems/cpRed/character/criticalInjuries/update/{injuryId}")
    public Map<String, Object> updateInjury(@PathVariable("injuryId") Long injuryId,
                                            @RequestBody CpRedCharacterCriticalInjuriesRequest cpRedCharacterCriticalInjuries) {
        return cpRedCharacterCriticalInjuriesService.updateInjury(injuryId, cpRedCharacterCriticalInjuries);
    }
    


}

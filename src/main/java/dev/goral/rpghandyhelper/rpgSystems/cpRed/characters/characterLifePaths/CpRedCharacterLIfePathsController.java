package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterLifePaths;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterLifePaths.CpRedCharacterLifePathsRequest;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterLifePaths.CpRedCharacterLifePathsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController 
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterLIfePathsController {
    private final CpRedCharacterLifePathsService cpRedCharacterLifePathsService;


    @GetMapping(path = "/rpgSystems/cpRed/character/lifepaths/all")
    public Map<String, Object> getAllLifePaths() {
        return cpRedCharacterLifePathsService.getAllLifePaths();
    }

    @GetMapping(path = "/rpgSystems/cpRed/character/lifepaths/{pathId}")
    public Map<String, Object> getLifePathById(@PathVariable("pathId") Long pathId) {
        return cpRedCharacterLifePathsService.getLifePathById(pathId);
    }

    @GetMapping(path = "/rpgSystems/cpRed/character/lifepaths/character/{characterId}")
    public Map<String, Object> getLifePathByCharacterId(@PathVariable("characterId") Long characterId) {
        return cpRedCharacterLifePathsService.getLifePathByCharacterId(characterId);
    }

    @GetMapping(path = "/admin/rpgSystems/cpRed/character/lifepaths/all")
    public Map<String, Object> getAllLifePathsForAdmin() {
        return cpRedCharacterLifePathsService.getAllLifePathsForAdmin();
    }

    @PostMapping(path = "/rpgSystems/cpRed/character/lifepaths/add")
    public Map<String, Object> addLifePath(@RequestBody CpRedCharacterLifePathsRequest cpRedCharacterLifePaths) {
        return cpRedCharacterLifePathsService.addLifePath(cpRedCharacterLifePaths);
    }

    @DeleteMapping(path = "/rpgSystems/cpRed/character/lifepaths/delete/{pathId}")
    public Map<String, Object> deleteLifePath(@PathVariable("pathId") Long pathId) {
        return cpRedCharacterLifePathsService.deleteLifePath(pathId);
    }

    @PutMapping(path = "/rpgSystems/cpRed/character/lifepaths/update/{pathId}")
    public Map<String, Object> updateLifePath(@PathVariable("pathId") Long pathId,
                                               @RequestBody CpRedCharacterLifePathsRequest cpRedCharacterLifePaths) {
        return cpRedCharacterLifePathsService.updateLifePath(pathId, cpRedCharacterLifePaths);
    }
}

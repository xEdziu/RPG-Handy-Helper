package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEnemies;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterEnemiesController {
    private final CpRedCharacterEnemiesService cpRedCharacterEnemiesService;

    @GetMapping(path = "/rpgSystems/cpRed/character/enemies/all")
    public Map<String, Object> getAllEnemies() {
        return cpRedCharacterEnemiesService.getAllEnemies();
    }

    @GetMapping(path = "/rpgSystems/cpRed/character/enemies/{enemyId}")
    public Map<String, Object> getEnemyById(@PathVariable ("enemyId") Long enemyId) {
        return cpRedCharacterEnemiesService.getEnemyById(enemyId);
    }

    @GetMapping(path = "/rpgSystems/cpRed/character/enemies/character/{characterId}")
    public Map<String, Object> getEnemiesByCharacterId(@PathVariable("characterId") Long characterId) {
        return cpRedCharacterEnemiesService.getEnemiesByCharacterId(characterId);
    }

    @GetMapping(path = "/admin/rpgSystems/cpRed/character/enemies/all")
    public Map<String, Object> getAllEnemiesForAdmin() {
        return cpRedCharacterEnemiesService.getAllEnemiesForAdmin();
    }

    @PostMapping(path = "/rpgSystems/cpRed/character/enemies/add")
    public Map<String, Object> addEnemy(@RequestBody CpRedCharacterEnemies cpRedCharacterEnemies) {
        return cpRedCharacterEnemiesService.addEnemy(cpRedCharacterEnemies);
    }

    @DeleteMapping(path = "/rpgSystems/cpRed/character/enemies/delete/{enemyId}")
    public Map<String, Object> deleteEnemy(@PathVariable("enemyId") Long enemyId) {
        return cpRedCharacterEnemiesService.deleteEnemy(enemyId);
    }

    @PutMapping(path = "/rpgSystems/cpRed/character/enemies/update/{enemyId}")
    public Map<String, Object> updateEnemy(@PathVariable("enemyId") Long enemyId,
                                           @RequestBody CpRedCharacterEnemies cpRedCharacterEnemies) {
        return cpRedCharacterEnemiesService.updateEnemy(enemyId, cpRedCharacterEnemies);
    }
}

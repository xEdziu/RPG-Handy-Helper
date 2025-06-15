package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomAmmunition;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterCustomAmmunitionController {
    private final CpRedCharacterCustomAmmunitionService characterCustomAmmunitionService;

    // ============ User methods ============
    @GetMapping(path = "/games/cpRed/characters/customAmmunition/{characterId}")
    public Map<String, Object> getCharacterCustomAmmunition(@PathVariable("characterId") Long characterId) {
        return characterCustomAmmunitionService.getCharacterCustomAmmunition(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/customAmmunition/create")
    public Map<String, Object> createCharacterCustomAmmunition(@RequestBody AddCharacterCustomAmmunitionRequest addCharacterCustomAmmunitionRequest) {
        return characterCustomAmmunitionService.createCharacterCustomAmmunition(addCharacterCustomAmmunitionRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/customAmmunition/update/{characterCustomAmmunitionId}")
    public Map<String, Object> updateCharacterCustomAmmunition(@PathVariable("characterCustomAmmunitionId") Long characterCustomAmmunitionId,
                                                               @RequestBody UpdateCharacterCustomAmmunitionRequest updateCharacterCustomAmmunitionRequest) {
        return characterCustomAmmunitionService.updateCharacterCustomAmmunition(characterCustomAmmunitionId, updateCharacterCustomAmmunitionRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/customAmmunition/delete/{characterCustomAmmunitionId}")
    public Map<String, Object> deleteCharacterCustomAmmunition(@PathVariable("characterCustomAmmunitionId") Long characterCustomAmmunitionId) {
        return characterCustomAmmunitionService.deleteCharacterCustomAmmunition(characterCustomAmmunitionId);
    }
    // ============ Admin methods ============
    @GetMapping(path = "/admin/games/cpRed/characters/customAmmunition/all")
    public Map<String, Object> getAllCharacterCustomAmmunition() {
        return characterCustomAmmunitionService.getAllCharacterCustomAmmunition();
    }
}

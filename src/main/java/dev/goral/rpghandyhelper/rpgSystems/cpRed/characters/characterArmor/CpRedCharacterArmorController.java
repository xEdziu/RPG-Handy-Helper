package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterArmorController {
    private final CpRedCharacterArmorService cpRedCharacterArmorService;

    // ============ User methods ============
    @GetMapping(path = "/games/cpRed/characters/armors/{characterId}")
    public Map<String, Object> getCharacterArmor(@PathVariable("characterId") Long characterId) {
        return cpRedCharacterArmorService.getCharacterArmor(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/armors/create")
    public Map<String, Object> createCharacterArmor(@RequestBody AddCharacterArmorRequest addCharacterArmorRequest) {
        return cpRedCharacterArmorService.createCharacterArmor(addCharacterArmorRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/armors/update/{characterArmorId}")
    public Map<String, Object> updateCharacterArmor(@PathVariable("characterArmorId") Long characterArmorId,
                                                     @RequestBody UpdateCharacterArmorRequest updateCharacterArmorRequest) {
        return cpRedCharacterArmorService.updateCharacterArmor(characterArmorId, updateCharacterArmorRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/armors/delete/{characterArmorId}")
    public Map<String, Object> deleteCharacterArmor(@PathVariable("characterArmorId") Long characterArmorId) {
        return cpRedCharacterArmorService.deleteCharacterArmor(characterArmorId);
    }

    // ============ Admin methods ============
    @GetMapping(path = "/admin/games/cpRed/characters/armors/all")
    public Map<String, Object> getAllCharacterArmors() {
        return cpRedCharacterArmorService.getAllCharacterArmors();
    }

}

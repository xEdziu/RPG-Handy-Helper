package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomArmor;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon.CpRedCharacterCustomWeaponService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterCustomArmorController {
    private final CpRedCharacterCustomArmorService cpRedCharacterCustomArmorService;

    @GetMapping(path = "/games/cpRed/characters/customArmors/{characterId}")
    public Map<String, Object> getCharacterCustomArmors(Long characterId) {
        return cpRedCharacterCustomArmorService.getCharacterCustomArmors(characterId);
    }

    @PutMapping(path = "/games/cpRed/characters/customArmors/update/{characterCustomArmorId}")
    public Map<String, Object> updateCharacterCustomArmor(@PathVariable("characterCustomArmorId") Long characterCustomArmorId,
                                                           @RequestBody UpdateCharacterCustomArmorRequest updateCharacterCustomArmorRequest) {
        return cpRedCharacterCustomArmorService.updateCharacterCustomArmor(characterCustomArmorId, updateCharacterCustomArmorRequest);
    }
    @PostMapping(path = "/games/cpRed/characters/customArmors/create")
    public Map<String, Object> createCharacterCustomArmor(@RequestBody AddCharacterCustomArmorRequest addCharacterCustomArmorRequest) {
        return cpRedCharacterCustomArmorService.createCharacterCustomArmor(addCharacterCustomArmorRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/customArmors/delete/{characterCustomArmorId}")
    public Map<String, Object> deleteCharacterCustomArmor(@PathVariable("characterCustomArmorId") Long characterCustomArmorId) {
        return cpRedCharacterCustomArmorService.deleteCharacterCustomArmor(characterCustomArmorId);
    }

    @GetMapping(path = "/admin/games/cpRed/characters/customArmors/all")
    public Map<String, Object> getAllCharacterCustomArmors() {
        return cpRedCharacterCustomArmorService.getAllCharacterCustomArmors();
    }
}

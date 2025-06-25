package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterWeaponController {
    private final CpRedCharacterWeaponService cpRedCharacterWeaponService;

    // ============ User methods ============
    @GetMapping(path = "/games/cpRed/characters/weapons/{characterId}")
    public Map<String, Object> getCharacterWeapons(@PathVariable("characterId")Long characterId) {
        return cpRedCharacterWeaponService.getCharacterWeapons(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/weapons/create")
    public Map<String, Object> createCharacterWeapon(@RequestBody AddCharacterWeaponRequest addCharacterWeaponRequest) {
        return cpRedCharacterWeaponService.createCharacterWeapon(addCharacterWeaponRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/weapons/update/{characterWeaponId}")
    public Map<String, Object> updateCharacterWeapon(@PathVariable("characterWeaponId") Long characterWeaponId,
                                                     @RequestBody UpdateCharacterWeaponRequest updateCharacterWeaponRequest) {
        return cpRedCharacterWeaponService.updateCharacterWeapon(characterWeaponId, updateCharacterWeaponRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/weapons/delete/{characterWeaponId}")
    public Map<String, Object> deleteCharacterWeapon(@PathVariable("characterWeaponId") Long characterWeaponId) {
        return cpRedCharacterWeaponService.deleteCharacterWeapon(characterWeaponId);
    }

    // ============ Admin methods ============
    @GetMapping(path = "/admin/games/cpRed/characters/weapons/all")
    public Map<String, Object> getAllCharacterWeapons() {
        return cpRedCharacterWeaponService.getAllCharacterWeapons();
    }


}

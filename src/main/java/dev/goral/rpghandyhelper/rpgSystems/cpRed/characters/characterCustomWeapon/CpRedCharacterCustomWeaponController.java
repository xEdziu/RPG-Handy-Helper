package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon.AddCharacterWeaponRequest;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon.UpdateCharacterWeaponRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterCustomWeaponController {
    private final CpRedCharacterCustomWeaponService cpRedCharacterCustomWeaponService;

    // ============ User methods ============
    @GetMapping(path = "/games/cpRed/characters/customWeapons/{characterId}")
    public Map<String, Object> getCharacterCustomWeapons(@PathVariable("characterId")Long characterId) {
        return cpRedCharacterCustomWeaponService.getCharacterCustomWeapons(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/customWeapons/create")
    public Map<String, Object> createCharacterCustomWeapon(@RequestBody AddCharacterCustomWeaponRequest addCharacterCustomWeaponRequest) {
        return cpRedCharacterCustomWeaponService.createCharacterCustomWeapon(addCharacterCustomWeaponRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/customWeapons/update/{characterCustomWeaponId}")
    public Map<String, Object> updateCharacterCustomWeapon(@PathVariable("characterCustomWeaponId") Long characterCustomWeaponId,
                                                     @RequestBody UpdateCharacterCustomWeaponRequest updateCharacterCustomWeaponRequest) {
        return cpRedCharacterCustomWeaponService.updateCharacterCustomWeapon(characterCustomWeaponId, updateCharacterCustomWeaponRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/customWeapons/delete/{characterCustomWeaponId}")
    public Map<String, Object> deleteCharacterCustomWeapon(@PathVariable("characterCustomWeaponId") Long characterCustomWeaponId) {
        return cpRedCharacterCustomWeaponService.deleteCharacterCustomWeapon(characterCustomWeaponId);
    }

    // ============ Admin methods ============
    @GetMapping(path = "/admin/games/cpRed/characters/customWeapons/all")
    public Map<String, Object> getAllCharacterCustomWeapons() {
        return cpRedCharacterCustomWeaponService.getAllCharacterCustomWeapons();
    }
}

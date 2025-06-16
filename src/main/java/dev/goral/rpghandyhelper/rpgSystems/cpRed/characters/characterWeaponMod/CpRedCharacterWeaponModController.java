package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeaponMod;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterWeaponModController {
    private final CpRedCharacterWeaponModService cpRedCharacterWeaponModService;

    // ============ User methods ============
    @GetMapping(path = "/games/cpRed/characters/weaponMods/{characterId}")
    public Map<String, Object> getCharacterWeaponMods(@PathVariable("characterId") Long characterId) {
        return cpRedCharacterWeaponModService.getCharacterWeaponMods(characterId);
    }

    @GetMapping(path = "/games/cpRed/characters/weaponMods/forWeapon")
    public Map<String, Object> getCharacterWeaponModsByCharacterWeaponId(@RequestBody GetModsForWeaponRequest getModsForWeaponRequest) {
        return cpRedCharacterWeaponModService.getCharacterWeaponModsByCharacterWeaponId(getModsForWeaponRequest);
    }

    @PostMapping(path = "/games/cpRed/characters/weaponMods/create")
    public Map<String, Object> createCharacterWeaponMod(@RequestBody AddCharacterWeaponModRequest addCharacterWeaponModRequest) {
        return cpRedCharacterWeaponModService.createCharacterWeaponMod(addCharacterWeaponModRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/weaponMods/delete/{characterWeaponModId}")
    public Map<String, Object> deleteCharacterWeaponMod(@PathVariable("characterWeaponModId") Long characterWeaponModId) {
        return cpRedCharacterWeaponModService.deleteCharacterWeaponMod(characterWeaponModId);
    }
    // ============ Admin methods ============
    @GetMapping(path = "/admin/games/cpRed/characters/weaponMods/all")
    public Map<String, Object> getAllCharacterWeaponMods() {
        return cpRedCharacterWeaponModService.getAllCharacterWeaponMods();
    }
}

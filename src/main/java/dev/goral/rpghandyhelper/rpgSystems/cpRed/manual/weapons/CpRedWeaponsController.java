package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedWeaponsController {
    private CpRedWeaponsService cpRedWeaponsService;

    // ============ User methods ============
    // Pobierz wszystkie bronie
    @GetMapping(path = "/rpgSystems/cpRed/weapons/all")
    public Map<String, Object> getAllWeapons() { // List<CpRedWeaponsDTO>
        return cpRedWeaponsService.getAllWeapons();
    }
    // Pobierz broń po id
    @GetMapping(path = "/rpgSystems/cpRed/weapons/{weaponId}")
    public Map<String, Object> getWeaponById(@PathVariable("weaponId") Long weaponId) { // CpRedWeaponsDTO
        return cpRedWeaponsService.getWeaponById(weaponId);
    }

    // ============ Admin methods ============
    // Pobierz wszystkie bronie dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/weapons/all")
    public Map<String, Object> getAllWeaponsForAdmin() { // List<CpRedWeapons>
        return cpRedWeaponsService.getAllWeaponsForAdmin();
    }
    // Dodać broń
    @PostMapping(path = "/admin/rpgSystems/cpRed/weapons/add")
    public Map<String, Object> addWeapon(@RequestBody AddWeaponRequest cpRedWeapons) {
        return cpRedWeaponsService.addWeapon(cpRedWeapons);
    }
    // Modyfikować broń
    @PutMapping(path = "/admin/rpgSystems/cpRed/weapons/update/{weaponId}")
    public Map<String, Object> updateWeapon(@PathVariable("weaponId") Long weaponId,
                                            @RequestBody AddWeaponRequest cpRedWeapons) {
        return cpRedWeaponsService.updateWeapon(weaponId, cpRedWeapons);
    }
    // Zmienić ukrywalność broni
    @PutMapping(path = "/admin/rpgSystems/cpRed/weapons/update/hide/{weaponId}")
    public Map<String, Object> hideWeapon(@PathVariable("weaponId") Long weaponId) {
        return cpRedWeaponsService.hideWeapon(weaponId);
    }
    // Zmienić modyfikowalność broni
    @PutMapping(path = "/admin/rpgSystems/cpRed/weapons/update/modifiable/{weaponId}")
    public Map<String, Object> modifiableWeapon(@PathVariable("weaponId") Long weaponId) {
        return cpRedWeaponsService.modifiableWeapon(weaponId);
    }
}

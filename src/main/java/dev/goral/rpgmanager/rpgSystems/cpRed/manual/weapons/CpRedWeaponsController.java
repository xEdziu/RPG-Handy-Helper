package dev.goral.rpgmanager.rpgSystems.cpRed.manual.weapons;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedWeaponsController {
    private CpRedWeaponsService cpRedWeaponsService;

    // ============ User methods ============
    // Pobierz wszystkie bronie
    @GetMapping(path = "/rpgSystems/cpRed/weapons/all")
    public List<CpRedWeaponsDTO> getAllWeapons() {
        return cpRedWeaponsService.getAllWeapons();
    }
    // Pobierz broń po id
    @GetMapping(path = "/rpgSystems/cpRed/weapons/{weaponId}")
    public CpRedWeaponsDTO getWeaponById(@PathVariable("weaponId") Long weaponId) {
        return cpRedWeaponsService.getWeaponById(weaponId);
    }

    // ============ Admin methods ============
    // Pobierz wszystkie bronie dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/weapons/all")
    public List<CpRedWeapons> getAllWeaponsForAdmin() {
        return cpRedWeaponsService.getAllWeaponsForAdmin();
    }
    // Dodać broń
    @PostMapping(path = "/admin/rpgSystems/cpRed/weapons/add")
    public Map<String, Object> addWeapon(@RequestBody CpRedWeapons cpRedWeapons) {
        return cpRedWeaponsService.addWeapon(cpRedWeapons);
    }
    // Modyfikować broń
    @PutMapping(path = "/admin/rpgSystems/cpRed/weapons/update/{weaponId}")
    public Map<String, Object> updateWeapon(@PathVariable("weaponId") Long weaponId, @RequestBody CpRedWeapons cpRedWeapons) {
        return cpRedWeaponsService.updateWeapon(weaponId, cpRedWeapons);
    }
}

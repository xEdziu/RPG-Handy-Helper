package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeaponMods;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCustomWeaponModsController {
    private final CpRedCustomWeaponModsService cpRedCustomWeaponModsService;

    @GetMapping(path = "/rpgSystems/cpRed/custom/weaponMods/all")
    public Map<String, Object> getAllWeaponMods() {
        return cpRedCustomWeaponModsService.getAllWeaponMods();
    }

    @GetMapping(path = "/rpgSystems/cpRed/custom/weaponMods/{weaponModId}")
    public Map<String, Object> getWeaponModById(@PathVariable("weaponModId") Long weaponModId) {
        return cpRedCustomWeaponModsService.getWeaponModById(weaponModId);
    }

    @PostMapping(path = "/rpgSystems/cpRed/custom/weaponMods/add")
    public Map<String, Object> addWeaponMod(@RequestBody CpRedCustomWeaponModsRequest cpRedCustomWeaponMods) {
        return cpRedCustomWeaponModsService.addWeaponMod(cpRedCustomWeaponMods);
    }

    @PutMapping(path = "/rpgSystems/cpRed/custom/weaponMods/update/{weaponModId}")
    public Map<String, Object> updateWeaponMod(@PathVariable("weaponModId") Long weaponModId,
                                               @RequestBody CpRedCustomWeaponModsRequest cpRedCustomWeaponMods) {
         return cpRedCustomWeaponModsService.updateWeaponMod(weaponModId, cpRedCustomWeaponMods);
     }

    @GetMapping(path = "/admin/rpgSystems/cpRed/custom/weaponMods/all")
    public Map<String, Object> getAllWeaponModsForAdmin() {
        return cpRedCustomWeaponModsService.getAllWeaponModsForAdmin();
    }
}

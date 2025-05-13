package dev.goral.rpgmanager.rpgSystems.cpRed.manual.weaponMods;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedWeaponModsController {
    private CpRedWeaponModsService cpRedWeaponModsService;

//    // ============ User methods ============
//    // Pobierz wszystkie modyfikacje broni
//    @GetMapping(path = "/rpgSystems/cpRed/weaponMods/all")
//    public Map<String, Object> getAllWeaponMods() { // List<CpRedWeaponModsDTO>
//        return cpRedWeaponModsService.getAllWeaponMods();
//    }
//    // Pobierz modyfikacje broni po id
//    @GetMapping(path = "/rpgSystems/cpRed/weaponMods/{weaponModId}")
//    public Map<String, Object> getWeaponModById(@PathVariable("weaponModId") Long weaponModId) { // CpRedWeaponModsDTO
//        return cpRedWeaponModsService.getWeaponModById(weaponModId);
//    }
//
//    // ============ Admin methods ============
//    // Pobierz wszystkie modyfikacje broni dla admina
//    @GetMapping(path = "/admin/rpgSystems/cpRed/weaponMods/all")
//    public Map<String, Object> getAllWeaponModsForAdmin() { // List<CpRedWeaponMods>
//        return cpRedWeaponModsService.getAllWeaponModsForAdmin();
//    }
//    // Dodaj modyfikacje broni
//    @GetMapping(path = "/admin/rpgSystems/cpRed/weaponMods/add")
//    public Map<String, Object> addWeaponMod(@RequestBody CpRedWeaponMods cpRedWeaponMods) {
//        return cpRedWeaponModsService.addWeaponMod(cpRedWeaponMods);
//    }
//    // Modyfikować modyfikacje broni
//    @GetMapping(path = "/admin/rpgSystems/cpRed/weaponMods/update/{weaponModId}")
//    public Map<String, Object> updateWeaponMod(@PathVariable("weaponModId") Long weaponModId,
//                                               @RequestBody CpRedWeaponMods cpRedWeaponMods) {
//        return cpRedWeaponModsService.updateWeaponMod(weaponModId, cpRedWeaponMods);
//    }

}

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

    // ============ User methods ============
    // Pobierz wszystkie modyfikacje broni
    @GetMapping(path = "/rpgSystems/cpRed/custom/weaponMods/all")
    public List<CpRedCustomWeaponModsDTO> getAllWeaponMods() {
        return cpRedCustomWeaponModsService.getAllWeaponMods();
    }
    // Pobierz modyfikację broni po id
    @GetMapping(path = "/rpgSystems/cpRed/custom/weaponMods/{weaponModId}")
    public CpRedCustomWeaponModsDTO getWeaponModById(@PathVariable("weaponModId") Long weaponModId) {
        return cpRedCustomWeaponModsService.getWeaponModById(weaponModId);
    }
    // Dodaj modyfikację broni
    @PostMapping(path = "/rpgSystems/cpRed/custom/weaponMods/add")
    public Map<String, Object> addWeaponMod(@RequestBody CpRedCustomWeaponMods cpRedCustomWeaponMods) {
        return cpRedCustomWeaponModsService.addWeaponMod(cpRedCustomWeaponMods);
    }
    // Modyfikować modyfikację broni
    @PutMapping(path = "/rpgSystems/cpRed/custom/weaponMods/update/{weaponModId}")
    public Map<String, Object> updateWeaponMod(@PathVariable("weaponModId") Long weaponModId,
                                               @RequestBody CpRedCustomWeaponMods cpRedCustomWeaponMods) {
         return cpRedCustomWeaponModsService.updateWeaponMod(weaponModId, cpRedCustomWeaponMods);
     }

    // ============ Admin methods ============
    // Pobierz wszystkie modyfikacje broni dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/custom/weaponMods/all")
    public List<CpRedCustomWeaponMods> getAllWeaponModsForAdmin() {
        return cpRedCustomWeaponModsService.getAllWeaponModsForAdmin();
    }


}

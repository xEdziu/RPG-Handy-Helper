package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeapons;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCustomWeaponsController {
    private final CpRedCustomWeaponsService cpRedCustomWeaponsService;

    // ============ User methods ============
    // Pobierz wszystkie customowe bronie
    @GetMapping(path = "/rpgSystems/cpRed/customWeapons/all")
    public Map<String, Object> getAllCustomWeapons() { // List<CpRedCustomWeaponsDTO>
        return cpRedCustomWeaponsService.getAllCustomWeapons();
    }

    // Pobierz customową broń po id
    @GetMapping(path = "/rpgSystems/cpRed/customWeapons/{customWeaponId}")
    public Map<String, Object> getCustomWeaponById(@PathVariable("customWeaponId") Long customWeaponId) { // CpRedCustomWeaponsDTO
        return cpRedCustomWeaponsService.getCustomWeaponById(customWeaponId);
    }

    // Pobierz wszystkie customowe bronie danej gry
    @GetMapping(path = "/rpgSystems/cpRed/customWeapons/game/{gameId}")
    public Map<String, Object> getAllCustomWeaponsByGameId(@PathVariable("gameId") Long gameId) { // List<CpRedCustomWeaponsDTO>
        return cpRedCustomWeaponsService.getAllCustomWeaponsByGameId(gameId);
    }

    // Dodaj customową broń
    @PostMapping(path = "/rpgSystems/cpRed/customWeapons/add")
    public Map<String, Object> addCustomWeapon(@RequestBody AddCustomWeaponRequest cpRedCustomWeapons) {
        return cpRedCustomWeaponsService.addCustomWeapon(cpRedCustomWeapons);
    }

    // Modyfikuj customową broń
    @PutMapping(path = "/rpgSystems/cpRed/customWeapons/update/{customWeaponId}")
    public Map<String, Object> updateCustomWeapon(@PathVariable("customWeaponId") Long customWeaponId,
                                                  @RequestBody AddCustomWeaponRequest cpRedCustomWeapons) {
        return cpRedCustomWeaponsService.updateCustomWeapon(customWeaponId, cpRedCustomWeapons);
    }

    // Modyfikuj ukrywalność broni
    @PutMapping(path = "/rpgSystems/cpRed/customWeapons/changeHide/{customWeaponId}")
    public Map<String, Object> changeHide(@PathVariable("customWeaponId") Long customWeaponId) {
        return cpRedCustomWeaponsService.changeHide(customWeaponId);
    }

    // Modyfikuj modyfikowalność broni
    @PutMapping(path = "/rpgSystems/cpRed/customWeapons/changeModifiable/{customWeaponId}")
    public Map<String, Object> changeModifiable(@PathVariable("customWeaponId") Long customWeaponId) {
        return cpRedCustomWeaponsService.changeModifiable(customWeaponId);
    }

    // ============ Admin methods ============
    // Pobierz wszystkie customowe bronie dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/customWeapons/all")
    public Map<String, Object> getAllCustomWeaponsForAdmin() { // List<CpRedCustomWeapons>
        return cpRedCustomWeaponsService.getAllCustomWeaponsForAdmin();
    }
}

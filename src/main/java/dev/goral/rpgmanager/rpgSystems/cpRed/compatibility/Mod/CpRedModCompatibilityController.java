package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.Mod;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedModCompatibilityController {
    private final CpRedModCompatibilityService modCompatibilityService;

    // ============ User methods ============
    @GetMapping(path = "/compatibility/mod/all")
    public Map<String, Object> allCompatibility() {
        return modCompatibilityService.getAllCompatibility();
    }

    @GetMapping(path = "/compatibility/mod/{id}")
    public Map<String, Object> getCompatibilityById(@PathVariable("id") Long id) {
        return modCompatibilityService.getCompatibilityById(id);
    }

    @GetMapping(path = "/compatibility/mod/forWeapon")
    public Map<String, Object> getCompatibilityByWeaponId(@RequestBody GetModCompatibilityByWeaponRequest getModCompatibilityByWeaponIdRequest) {
        return modCompatibilityService.getCompatibilityByWeaponId(getModCompatibilityByWeaponIdRequest);
    }

    @GetMapping(path = "/compatibility/mod/forMod")
    public Map<String, Object> getCompatibilityByModId(@RequestBody GetModCompatibilityByModRequest getModCompatibilityByModRequest) {
        return modCompatibilityService.getCompatibilityByModId(getModCompatibilityByModRequest);
    }

    @PostMapping(path = "/compatibility/mod/add")
    public Map<String, Object> addCompatibility(
            @RequestBody AddModCompatibilityRequest addModCompatibilityRequest) {
        return modCompatibilityService.addCompatibility(addModCompatibilityRequest);
    }

    @PutMapping(path = "/compatibility/mod/update/{id}")
    public Map<String, Object> updateCompatibility(
            @PathVariable("id") Long id,
            @RequestBody AddModCompatibilityRequest addModCompatibilityRequest) {
        return modCompatibilityService.updateCompatibility(id, addModCompatibilityRequest);
    }

    // ============ Admin methods ============
}


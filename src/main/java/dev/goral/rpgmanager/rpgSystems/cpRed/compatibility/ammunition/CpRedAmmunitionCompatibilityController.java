package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedAmmunitionCompatibilityController {
    private final CpRedAmmunitionCompatibilityService ammunitionCompatibilityService;

    // ============ User methods ============
    @GetMapping(path = "/compatibility/ammunition/all")
    public Map<String, Object> allCompatibility() {
        return ammunitionCompatibilityService.getAllCompatibility();
    }

    @GetMapping(path = "/compatibility/ammunition/{id}")
    public Map<String, Object> getCompatibilityById(@PathVariable("id") Long id) {
        return ammunitionCompatibilityService.getCompatibilityById(id);
    }

    @GetMapping(path = "/compatibility/ammunition/forWeapon")
    public Map<String, Object> getCompatibilityByWeaponId(@RequestBody GetCompatibilityByWeaponRequest getCompatibilityByWeaponIdRequest) {
        return ammunitionCompatibilityService.getCompatibilityByWeaponId(getCompatibilityByWeaponIdRequest);
    }

    @GetMapping(path = "/compatibility/ammunition/forAmmunition")
    public Map<String, Object> getCompatibilityByAmmunitionId(@RequestBody GetCompatibilityByAmmunitionRequest getCompatibilityByAmmunitionRequest) {
        return ammunitionCompatibilityService.getCompatibilityByAmmunitionId(getCompatibilityByAmmunitionRequest);
    }

    @PostMapping(path = "/compatibility/ammunition/add")
    public Map<String, Object> addCompatibility(
            @RequestBody AddAmmunitionCompatibilityRequest addAmmunitionCompatibilityRequest) {
        return ammunitionCompatibilityService.addCompatibility(addAmmunitionCompatibilityRequest);
    }

    @PutMapping(path = "/compatibility/ammunition/update/{id}")
    public Map<String, Object> updateCompatibility(
            @PathVariable("id") Long id,
            @RequestBody AddAmmunitionCompatibilityRequest addAmmunitionCompatibilityRequest) {
        return ammunitionCompatibilityService.updateCompatibility(id, addAmmunitionCompatibilityRequest);
    }

    // ============ Admin methods ============
}

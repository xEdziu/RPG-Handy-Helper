package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedAmmunitionCompatibilityController {
    private final CpRedAmmunitionCompatibilityService ammunitionCompatibilityService;

    // ============ User methods ============
    @PostMapping(path = "/compatibility/ammunition/add")
    public Map<String, Object> addCompatibility(
            @RequestBody CpRedAmmunitionCompatibilityRequestDTO ammunitionCompatibilityRequestDTO) {
        return ammunitionCompatibilityService.addCompatibility(
                ammunitionCompatibilityRequestDTO.getWeaponId(),
                ammunitionCompatibilityRequestDTO.getAmmoId(),
                ammunitionCompatibilityRequestDTO.isWeaponCustom(),
                ammunitionCompatibilityRequestDTO.isAmmoCustom());

    }

    // ============ Admin methods ============
}

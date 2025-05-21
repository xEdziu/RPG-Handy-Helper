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
        return ammunitionCompatibilityService.allCompatibility();
    }

    @PostMapping(path = "/compatibility/ammunition/add")
    public Map<String, Object> addCompatibility(
            @RequestBody AddAmmunitionCompatibilityRequest addAmmunitionCompatibilityRequest) {
        return ammunitionCompatibilityService.addCompatibility(addAmmunitionCompatibilityRequest);
    }

    // ============ Admin methods ============
}

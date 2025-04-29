package dev.goral.rpgmanager.rpgSystems.cpRed.manual.armors;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedArmorsController {
    private CpRedArmorsService cpRedArmorsService;

    // ============ User methods ============
    // Pobierz wszystkie zbroje
    @GetMapping(path = "/rpgSystems/cpRed/armors/all")
    public List<CpRedArmorsDTO> getAllArmors() {
        return cpRedArmorsService.getAllArmors();
    }
    // Pobierz zbroję po id
    @GetMapping(path = "/rpgSystems/cpRed/armors/{armorId}")
    public CpRedArmorsDTO getArmorById(@PathVariable("armorId") Long armorId) {
        return cpRedArmorsService.getArmorById(armorId);
    }

    // ============ Admin methods ============
    // Pobierz wszystkie zbroje dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/armors/all")
    public List<CpRedArmors> getAllArmorsForAdmin() {
        return cpRedArmorsService.getAllArmorsForAdmin();
    }
    // Dodać zbroję
    @PostMapping(path = "/admin/rpgSystems/cpRed/armors/add")
    public Map<String, Object> addArmor(@RequestBody CpRedArmors cpRedArmors) {
        return cpRedArmorsService.addArmor(cpRedArmors);
    }
    // Modyfikować zbroję
    @PutMapping(path = "/admin/rpgSystems/cpRed/armors/update/{armorId}")
    public Map<String, Object> updateArmor(@PathVariable("armorId") Long armorId,
                                           @RequestBody CpRedArmors cpRedArmors) {
        return cpRedArmorsService.updateArmor(armorId, cpRedArmors);
    }
}

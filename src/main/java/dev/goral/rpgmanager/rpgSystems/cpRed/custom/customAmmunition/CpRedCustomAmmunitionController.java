package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customAmmunition;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCustomAmmunitionController {
    private final CpRedCustomAmmunitionService cpRedCustomAmmunitionService;

    // ============ User methods ============
    // Pobierz wszystkie amunicje
    @GetMapping(path = "/rpgSystems/cpRed/customAmmunition/all")
    public List<CpRedCustomAmmunitionDTO> getAllCustomAmmunition() {
        return cpRedCustomAmmunitionService.getAllCustomAmmunition();
    }
    // Pobierz amunicje po id
    @GetMapping(path = "/rpgSystems/cpRed/customAmmunition/{ammunitionId}")
    public CpRedCustomAmmunitionDTO getCustomAmmunitionById(@PathVariable("ammunitionId") Long ammunitionId) {
        return cpRedCustomAmmunitionService.getCustomAmmunitionById(ammunitionId);
    }
    // Dodaj amunicje
    @PostMapping(path = "/admin/rpgSystems/cpRed/customAmmunition/add")
    public CpRedCustomAmmunitionDTO addCustomAmmunition(@RequestBody CpRedCustomAmmunition cpRedCustomAmmunition) {
        return cpRedCustomAmmunitionService.addCustomAmmunition(cpRedCustomAmmunition);
    }
    // Modyfikuj amunicje
    @PutMapping(path = "/admin/rpgSystems/cpRed/customAmmunition/update/{ammunitionId}")
    public CpRedCustomAmmunitionDTO updateCustomAmmunition(@PathVariable("ammunitionId") Long ammunitionId,
                                                           @RequestBody CpRedCustomAmmunition cpRedCustomAmmunition) {
        return cpRedCustomAmmunitionService.updateCustomAmmunition(ammunitionId, cpRedCustomAmmunition);
    }

    // ============ Admin methods ============
    // Pobierz wszystkie amunicje dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/customAmmunition/all")
    public List<CpRedCustomAmmunition> getAllCustomAmmunitionForAdmin() {
        return cpRedCustomAmmunitionService.getAllCustomAmmunitionForAdmin();
    }
}

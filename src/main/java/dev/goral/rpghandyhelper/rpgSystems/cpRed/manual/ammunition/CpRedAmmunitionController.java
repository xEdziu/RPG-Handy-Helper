package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedAmmunitionController {
    private CpRedAmmunitionService cpRedAmmunitionService;

    // ============ User methods ============
    // Pobierz wszystkie amunicje
    @GetMapping(path = "/rpgSystems/cpRed/ammunition/all")
    public Map<String, Object> getAllAmmunition() { // List<CpRedAmmunitionDTO>
        return cpRedAmmunitionService.getAllAmmunition();
    }
    // Pobierz amunicje po id
    @GetMapping(path = "/rpgSystems/cpRed/ammunition/{ammunitionId}")
    public Map<String, Object> getAmmunitionById(@PathVariable("ammunitionId") Long ammunitionId) { // CpRedAmmunitionDTO
        return cpRedAmmunitionService.getAmmunitionById(ammunitionId);
    }

    // ============ Admin methods ============
    // Pobierz wszystkie amunicje dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/ammunition/all")
    public Map<String, Object> getAllAmmunitionForAdmin() { // List<CpRedAmmunition>
        return cpRedAmmunitionService.getAllAmmunitionForAdmin();
    }
    // Dodać amunicje
    @PostMapping(path = "/admin/rpgSystems/cpRed/ammunition/add")
    public Map<String, Object> addAmmunition(@RequestBody CpRedAmmunition cpRedAmmunition) {
        return cpRedAmmunitionService.addAmmunition(cpRedAmmunition);
    }
    // Modyfikować amunicje
    @PutMapping(path = "/admin/rpgSystems/cpRed/ammunition/update/{ammunitionId}")
    public Map<String, Object> updateAmmunition(@PathVariable("ammunitionId") Long ammunitionId,
                                                @RequestBody CpRedAmmunition cpRedAmmunition) {
        return cpRedAmmunitionService.updateAmmunition(ammunitionId, cpRedAmmunition);
    }
}

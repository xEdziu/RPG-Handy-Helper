package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customAmmunition;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCustomAmmunitionController {
    private final CpRedCustomAmmunitionService cpRedCustomAmmunitionService;

    // ============ User methods ============
    // Pobierz wszystkie amunicje
    @GetMapping(path = "/rpgSystems/cpRed/customAmmunition/all")
    // List<CpRedCustomAmmunitionDTO>
    public Map<String, Object> getAllCustomAmmunition() {
        return cpRedCustomAmmunitionService.getAllCustomAmmunition();
    }
    // Pobierz amunicje po id
    @GetMapping(path = "/rpgSystems/cpRed/customAmmunition/{ammunitionId}")
    // CpRedCustomAmmunitionDTO
    public Map<String, Object> getCustomAmmunitionById(@PathVariable("ammunitionId") Long ammunitionId) {
        return cpRedCustomAmmunitionService.getCustomAmmunitionById(ammunitionId);
    }
    // Pobierz amunicje po grze
    @GetMapping(path = "/rpgSystems/cpRed/customAmmunition/game/{gameId}")
    // List<CpRedCustomAmmunitionDTO>
    public Map<String, Object> getCustomAmmunitionByGameId(@PathVariable("gameId") Long gameId) {
        return cpRedCustomAmmunitionService.getCustomAmmunitionByGameId(gameId);
    }
    // Dodaj amunicje
    @PostMapping(path = "/rpgSystems/cpRed/customAmmunition/add")
    public Map<String, Object> addCustomAmmunition(@RequestBody AddCustomAmmunitionRequest cpRedCustomAmmunition) {
        return cpRedCustomAmmunitionService.addCustomAmmunition(cpRedCustomAmmunition);
    }
    // Modyfikuj amunicje
    @PutMapping(path = "/rpgSystems/cpRed/customAmmunition/update/{ammunitionId}")
    public Map<String, Object> updateCustomAmmunition(@PathVariable("ammunitionId") Long ammunitionId,
                                                      @RequestBody AddCustomAmmunitionRequest cpRedCustomAmmunition) {
        return cpRedCustomAmmunitionService.updateCustomAmmunition(ammunitionId, cpRedCustomAmmunition);
    }

    // ============ Admin methods ============
    // Pobierz wszystkie amunicje dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/customAmmunition/all")
    public Map<String, Object> getAllCustomAmmunitionForAdmin() {
        return cpRedCustomAmmunitionService.getAllCustomAmmunitionForAdmin();
    }
}

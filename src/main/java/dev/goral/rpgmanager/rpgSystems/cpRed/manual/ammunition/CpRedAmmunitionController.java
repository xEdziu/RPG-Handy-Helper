package dev.goral.rpgmanager.rpgSystems.cpRed.manual.ammunition;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedAmmunitionController {
    private CpRedAmmunitionService cpRedAmmunitionService;

//    // ============ User methods ============
//    // Pobierz wszystkie amunicje
//    @GetMapping(path = "/rpgSystems/cpRed/ammunition/all")
//    public List<CpRedAmmunitionDTO> getAllAmmunition() {
//        return cpRedAmmunitionService.getAllAmmunition();
//    }
//    // Pobierz amunicje po id
//    @GetMapping(path = "/rpgSystems/cpRed/ammunition/{ammunitionId}")
//    public CpRedAmmunitionDTO getAmmunitionById(@PathVariable("ammunitionId") Long ammunitionId) {
//        return cpRedAmmunitionService.getAmmunitionById(ammunitionId);
//    }
//
//    // ============ Admin methods ============
//    // Pobierz wszystkie amunicje dla admina
//    @GetMapping(path = "/admin/rpgSystems/cpRed/ammunition/all")
//    public List<CpRedAmmunition> getAllAmmunitionForAdmin() {
//        return cpRedAmmunitionService.getAllAmmunitionForAdmin();
//    }
//    // Dodać amunicje
//    @PostMapping(path = "/admin/rpgSystems/cpRed/ammunition/add")
//    public Map<String, Object> addAmmunition(CpRedAmmunition cpRedAmmunition) {
//        return cpRedAmmunitionService.addAmmunition(cpRedAmmunition);
//    }
//    // Modyfikować amunicje
//    @PutMapping(path = "/admin/rpgSystems/cpRed/ammunition/update/{ammunitionId}")
//    public Map<String, Object> updateAmmunition(@PathVariable("ammunitionId") Long ammunitionId,
//                                                @RequestBody CpRedAmmunition cpRedAmmunition) {
//        return cpRedAmmunitionService.updateAmmunition(ammunitionId, cpRedAmmunition);
//    }
}

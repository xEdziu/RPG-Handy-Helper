package dev.goral.rpgmanager.rpgSystems.cpRed.manual.criticalInjuries;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCriticalInjuriesController {
    private CpRedCriticalInjuriesService cpRedCriticalInjuriesService;

//    // ============ User methods ============
//    // Pobierz wszystkie obrażenia krytyczne
//    @GetMapping(path = "/rpgSystems/cpRed/criticalInjuries/all")
//    public List<CpRedCriticalInjuriesDTO> getAllCriticalInjuries() {
//        return cpRedCriticalInjuriesService.getAllCriticalInjuries();
//    }
//    // Pobierz obrażenia krytyczne po id
//    @GetMapping(path = "/rpgSystems/cpRed/criticalInjuries/{injuryId}")
//    public CpRedCriticalInjuriesDTO getCriticalInjuryById(@PathVariable("injuryId") Long injuryId) {
//        return cpRedCriticalInjuriesService.getCriticalInjuryById(injuryId);
//    }
//
//    // ============ Admin methods ============
//    // Pobierz wszystkie obrażenia krytyczne dla admina
//    @GetMapping(path = "/admin/rpgSystems/cpRed/criticalInjuries/all")
//    public List<CpRedCriticalInjuries> getAllCriticalInjuriesForAdmin() {
//        return cpRedCriticalInjuriesService.getAllCriticalInjuriesForAdmin();
//    }
//    // Dodać obrażenia krytyczne
//    @PostMapping(path = "/admin/rpgSystems/cpRed/criticalInjuries/add")
//    public Map<String, Object> addCriticalInjury(@RequestBody CpRedCriticalInjuries cpRedCriticalInjuries) {
//        return cpRedCriticalInjuriesService.addCriticalInjury(cpRedCriticalInjuries);
//    }
//    // Modyfikować obrażenia krytyczne
//    @PutMapping(path = "/admin/rpgSystems/cpRed/criticalInjuries/update/{injuryId}")
//    public Map<String, Object> updateCriticalInjury(@PathVariable("injuryId") Long injuryId,
//                                                    @RequestBody CpRedCriticalInjuries cpRedCriticalInjuries) {
//        return cpRedCriticalInjuriesService.updateCriticalInjury(injuryId, cpRedCriticalInjuries);
//    }
}

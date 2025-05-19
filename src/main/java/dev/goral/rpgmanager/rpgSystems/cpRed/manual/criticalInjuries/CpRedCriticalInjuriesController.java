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

    @GetMapping(path = "/rpgSystems/cpRed/criticalInjuries/all")
    public Map<String, Object> getAllCriticalInjuries() { // List<CpRedCriticalInjuriesDTO>
        return cpRedCriticalInjuriesService.getAllCriticalInjuries();
    }

    @GetMapping(path = "/rpgSystems/cpRed/criticalInjuries/{injuryId}")
    public Map<String, Object> getCriticalInjuryById(@PathVariable("injuryId") Long injuryId) { // CpRedCriticalInjuriesDTO
        return cpRedCriticalInjuriesService.getCriticalInjuryById(injuryId);
    }

    @GetMapping(path = "/admin/rpgSystems/cpRed/criticalInjuries/all")
    public Map<String, Object> getAllCriticalInjuriesForAdmin() { // List<CpRedCriticalInjuries>
        return cpRedCriticalInjuriesService.getAllCriticalInjuriesForAdmin();
    }

    @PostMapping(path = "/admin/rpgSystems/cpRed/criticalInjuries/add")
    public Map<String, Object> addCriticalInjury(@RequestBody CpRedCriticalInjuries cpRedCriticalInjuries) {
        return cpRedCriticalInjuriesService.addCriticalInjury(cpRedCriticalInjuries);
    }

    @PutMapping(path = "/admin/rpgSystems/cpRed/criticalInjuries/update/{injuryId}")
    public Map<String, Object> updateCriticalInjury(@PathVariable("injuryId") Long injuryId,
                                                    @RequestBody CpRedCriticalInjuries cpRedCriticalInjuries) {
        return cpRedCriticalInjuriesService.updateCriticalInjury(injuryId, cpRedCriticalInjuries);
    }
}

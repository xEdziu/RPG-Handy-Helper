package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCriticalInjuries;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCustomCriticalInjuriesController {
    private final CpRedCustomCriticalInjuriesService cpRedCustomCriticalInjuriesService;


    @GetMapping(path = "/rpgSystems/cpRed/customCriticalInjuries/all")
    public Map<String, Object> getAllCustomCriticalInjuries() { // List<CpRedCustomCriticalInjuriesDTO>
        return cpRedCustomCriticalInjuriesService.getAllCustomCriticalInjuries();
    }

    @GetMapping(path = "/rpgSystems/cpRed/customCriticalInjuries/{customCriticalInjuryId}")
    public Map<String, Object> getCustomCriticalInjuryById(
            @PathVariable("customCriticalInjuryId") Long customCriticalInjuryId) { // CpRedCustomCriticalInjuriesDTO
        return cpRedCustomCriticalInjuriesService.getCustomCriticalInjuryById(customCriticalInjuryId);
    }

    @GetMapping(path = "/rpgSystems/cpRed/customCriticalInjuries/game/{gameId}")
    public Map<String, Object> getCustomCriticalInjuryByGame(@PathVariable("gameId") Long gameId) {
        return cpRedCustomCriticalInjuriesService.getCustomCriticalInjuryByGame(gameId);
    }

    @PostMapping(path = "/rpgSystems/cpRed/customCriticalInjuries/add")
    public Map<String, Object> addCustomCriticalInjury(
            @RequestBody CpRedCustomCriticalInjuriesRequest cpRedCustomCriticalInjuries) {
        return cpRedCustomCriticalInjuriesService.addCustomCriticalInjury(cpRedCustomCriticalInjuries);
    }

    @PutMapping(path = "/rpgSystems/cpRed/customCriticalInjuries/update/{customCriticalInjuryId}")
    public Map<String, Object> updateCustomCriticalInjury(@PathVariable("customCriticalInjuryId") Long customCriticalInjuryId,
                                                           @RequestBody CpRedCustomCriticalInjuriesRequest cpRedCustomCriticalInjuries) {
        return cpRedCustomCriticalInjuriesService.updateCustomCriticalInjury(customCriticalInjuryId, cpRedCustomCriticalInjuries);
    }
    @GetMapping(path = "/admin/rpgSystems/cpRed/customCriticalInjuries/all")
    public Map<String, Object> getAllCustomCriticalInjuriesForAdmin() { // List<CpRedCustomCriticalInjuries>
        return cpRedCustomCriticalInjuriesService.getAllCustomCriticalInjuriesForAdmin();
    }
}

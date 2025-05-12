package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customCriticalInjuries;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCustomCriticalInjuriesController {
    private final CpRedCustomCriticalInjuriesService cpRedCustomCriticalInjuriesService;

//    // ============ User methods ============
//    // Pobierz wszystkie customowe rany krytyczne
//    @GetMapping(path = "/rpgSystems/cpRed/customCriticalInjuries/all")
//    public Map<String, Object> getAllCustomCriticalInjuries() { // List<CpRedCustomCriticalInjuriesDTO>
//        return cpRedCustomCriticalInjuriesService.getAllCustomCriticalInjuries();
//    }
//    // Pobierz customową ranę krytyczną po id
//    @GetMapping(path = "/rpgSystems/cpRed/customCriticalInjuries/{customCriticalInjuryId}")
//    public Map<String, Object> getCustomCriticalInjuryById(
//            @PathVariable("customCriticalInjuryId") Long customCriticalInjuryId) { // CpRedCustomCriticalInjuriesDTO
//        return cpRedCustomCriticalInjuriesService.getCustomCriticalInjuryById(customCriticalInjuryId);
//    }
//    // Dodać customową ranę krytyczną
//    @PostMapping(path = "/rpgSystems/cpRed/customCriticalInjuries/add")
//    public Map<String, Object> addCustomCriticalInjury(
//            @RequestBody CpRedCustomCriticalInjuries cpRedCustomCriticalInjuries) {
//        return cpRedCustomCriticalInjuriesService.addCustomCriticalInjury(cpRedCustomCriticalInjuries);
//    }
//    // Modyfikować customową ranę krytyczną
//    @PutMapping(path = "/rpgSystems/cpRed/customCriticalInjuries/update/{customCriticalInjuryId}")
//    public Map<String, Object> updateCustomCriticalInjury(@PathVariable("customCriticalInjuryId") Long customCriticalInjuryId,
//                                                           @RequestBody CpRedCustomCriticalInjuries cpRedCustomCriticalInjuries) {
//        return cpRedCustomCriticalInjuriesService.updateCustomCriticalInjury(customCriticalInjuryId, cpRedCustomCriticalInjuries);
//    }
//
//    // ============ Admin methods ============
//    // Pobierz wszystkie customowe rany krytyczne dla admina
//    @GetMapping(path = "/admin/rpgSystems/cpRed/customCriticalInjuries/all")
//    public Map<String, Object> getAllCustomCriticalInjuriesForAdmin() { // List<CpRedCustomCriticalInjuries>
//        return cpRedCustomCriticalInjuriesService.getAllCustomCriticalInjuriesForAdmin();
//    }
}

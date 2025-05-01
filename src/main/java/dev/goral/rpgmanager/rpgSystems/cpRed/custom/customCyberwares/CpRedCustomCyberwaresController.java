package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customCyberwares;

import dev.goral.rpgmanager.rpgSystems.cpRed.manual.classes.CpRedClassesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCustomCyberwaresController {
    private CpRedCustomCyberwaresService cpRedCustomCyberwaresService;

//    // ============ User methods ============
//    // Pobierz wszystkie cyberware
//    @GetMapping(path = "/rpgSystems/cpRed/customCyberware/all")
//    public List<CpRedCustomCyberwaresDTO> getAllCyberware() {
//        return cpRedCustomCyberwaresService.getAllCyberware();
//    }
//    // Pobierz cyberware po id
//    @GetMapping(path = "/rpgSystems/cpRed/customCyberware/{cyberwareId}")
//    public CpRedCustomCyberwaresDTO getCyberwareById(@PathVariable("cyberwareId") Long cyberwareId) {
//        return cpRedCustomCyberwaresService.getCyberwareById(cyberwareId);
//    }
//    // Doadaj cyberware
//    @PostMapping(path = "/rpgSystems/cpRed/customCyberware/add")
//    public Map<String, Object> addCyberware(@RequestBody CpRedCustomCyberwares cpRedCustomCyberwares) {
//        return cpRedCustomCyberwaresService.addCyberware(cpRedCustomCyberwares);
//    }
//    // Modyfikować cyberware
//    @PutMapping(path = "/rpgSystems/cpRed/customCyberware/update/{cyberwareId}")
//    public Map<String, Object> updateCyberware(@PathVariable("cyberwareId") Long cyberwareId,
//                                               @RequestBody CpRedCustomCyberwares cpRedCustomCyberwares) {
//        return cpRedCustomCyberwaresService.updateCyberware(cyberwareId, cpRedCustomCyberwares);
//    }
//
//    // ============ Admin methods ============
//    // Pobierz wszystkie cyberware dla admina
//    @GetMapping(path = "/admin/rpgSystems/cpRed/customCyberware/all")
//    public List<CpRedCustomCyberwares> getAllCyberwareForAdmin() {
//        return cpRedCustomCyberwaresService.getAllCyberwareForAdmin();
//    }
}

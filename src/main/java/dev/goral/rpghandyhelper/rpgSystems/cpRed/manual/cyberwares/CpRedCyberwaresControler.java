package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.cyberwares;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCyberwaresControler {
    private CpRedCyberwaresService cpRedCyberwaresService;

    // ============ User methods ============
    // Pobierz wszystkie cyberware
    @GetMapping(path = "/rpgSystems/cpRed/cyberwares/all")
    public Map<String,Object> getAllCyberwares() {
        return cpRedCyberwaresService.getAllCyberwares();
    }
    // Pobierz cyberware po id
    @GetMapping(path = "/rpgSystems/cpRed/cyberwares/{cyberwareId}")
    public Map<String,Object> getCyberwareById(@PathVariable("cyberwareId") Long cyberwareId) {
        return cpRedCyberwaresService.getCyberwareById(cyberwareId);
    }

    // ============ Admin methods ============
    // Pobierz wszystkie cyberware dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/cyberwares/all")
    public Map<String,Object> getAllCyberwaresForAdmin() {
        return cpRedCyberwaresService.getAllCyberwaresForAdmin();
    }
    // Dodać cyberware
    @PostMapping(path = "/admin/rpgSystems/cpRed/cyberwares/add")
    public Map<String, Object> addCyberware(@RequestBody CpRedCyberwares cpRedCyberwares) {
        return cpRedCyberwaresService.addCyberware(cpRedCyberwares);
    }
    // Modyfikować cyberware
    @PutMapping(path = "/admin/rpgSystems/cpRed/cyberwares/update/{cyberwareId}")
    public Map<String, Object> updateCyberware(@PathVariable("cyberwareId") Long cyberwareId,
                                               @RequestBody CpRedCyberwares cpRedCyberwares) {
        return cpRedCyberwaresService.updateCyberware(cyberwareId, cpRedCyberwares);
    }
}
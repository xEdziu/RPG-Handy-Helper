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

    @GetMapping(path = "/rpgSystems/cpRed/customCyberware/all")
    public Map<String, Object> getAllCyberware() {
        return cpRedCustomCyberwaresService.getAllCyberware();
    }

    @GetMapping(path = "/rpgSystems/cpRed/customCyberware/{cyberwareId}")
    public Map<String, Object> getCyberwareById(@PathVariable("cyberwareId") Long cyberwareId) {
        return cpRedCustomCyberwaresService.getCyberwareById(cyberwareId);
    }
    @GetMapping(path = "/rpgSystems/cpRed/customCyberware/game/{gameId}")
    public Map<String, Object> getCyberwareByGame(@PathVariable("gameId") Long gameId) {
        return cpRedCustomCyberwaresService.getCyberwareByGame(gameId);
    }


    @PostMapping(path = "/rpgSystems/cpRed/customCyberware/add")
    public Map<String, Object> addCyberware(@RequestBody CpRedCustomCyberwaresRequest cpRedCustomCyberwares) {
        return cpRedCustomCyberwaresService.addCyberware(cpRedCustomCyberwares);
    }

    @PutMapping(path = "/rpgSystems/cpRed/customCyberware/update/{cyberwareId}")
    public Map<String, Object> updateCyberware(@PathVariable("cyberwareId") Long cyberwareId,
                                               @RequestBody CpRedCustomCyberwaresRequest cpRedCustomCyberwares) {
        return cpRedCustomCyberwaresService.updateCyberware(cyberwareId, cpRedCustomCyberwares);
    }

    @GetMapping(path = "/admin/rpgSystems/cpRed/customCyberware/all")
    public Map<String, Object> getAllCyberwareForAdmin() {
        return cpRedCustomCyberwaresService.getAllCyberwareForAdmin();
    }
}

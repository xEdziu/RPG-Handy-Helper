package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors;

import dev.goral.rpgmanager.game.Game;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCustomArmorsController {
    private final CpRedCustomArmorsService cpRedCustomArmorsService;

    @GetMapping(path = "/rpgSystems/cpRed/customArmors/all")
    public Map<String, Object> getAllCustomArmors() {
        return cpRedCustomArmorsService.getAllCustomArmors();
    }

    @GetMapping(path = "/rpgSystems/cpRed/customArmors/{armorId}")
    public Map<String, Object> getCustomArmorById(@PathVariable("armorId") Long armorId) {
        return cpRedCustomArmorsService.getCustomArmorById(armorId);
    }

    @GetMapping(path = "/rpgSystems/cpRed/customArmors/game/{gameId}")
    public Map<String, Object> getCustomArmorByGame(@PathVariable("gameId") Long gameId) {
        return cpRedCustomArmorsService.getCustomArmorByGame(gameId);
    }

    @PostMapping(path = "/rpgSystems/cpRed/customArmors/add")
    public Map<String, Object> addCustomArmor(@RequestBody CpRedCustomArmorsRequest cpRedCustomArmors) {
        return cpRedCustomArmorsService.addCustomArmor(cpRedCustomArmors);
    }

    @PutMapping(path = "/rpgSystems/cpRed/customArmors/update/{armorId}")
    public Map<String, Object> updateCustomArmor(@PathVariable("armorId") Long armorId,
                                                 @RequestBody CpRedCustomArmorsRequest cpRedCustomArmors) {
        return cpRedCustomArmorsService.updateCustomArmor(armorId, cpRedCustomArmors);
    }

    @GetMapping(path = "/admin/rpgSystems/cpRed/customArmors/all")
    public Map<String, Object> getAllCustomArmorsForAdmin() {
        return cpRedCustomArmorsService.getAllCustomArmorsForAdmin();
    }
}

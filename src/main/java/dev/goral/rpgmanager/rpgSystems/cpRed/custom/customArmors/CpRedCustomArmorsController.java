package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCustomArmorsController {
    private final CpRedCustomArmorsService cpRedCustomArmorsService;

//    // ============ User methods ============
//    // Pobierz wszystkie customowe zbroje
//    @GetMapping(path = "/rpgSystems/cpRed/customArmors/all")
//    public List<CpRedCustomArmorsDTO> getAllCustomArmors() {
//        return cpRedCustomArmorsService.getAllCustomArmors();
//    }
//    // Pobierz customową zbroję po id
//    @GetMapping(path = "/rpgSystems/cpRed/customArmors/{armorId}")
//    public CpRedCustomArmorsDTO getCustomArmorById(@PathVariable("armorId") Long armorId) {
//        return cpRedCustomArmorsService.getCustomArmorById(armorId);
//    }
//    // Dodaj customową zbroję
//    @GetMapping(path = "/rpgSystems/cpRed/customArmors/add")
//    public Map<String, Object> addCustomArmor(CpRedCustomArmors cpRedCustomArmors) {
//        return cpRedCustomArmorsService.addCustomArmor(cpRedCustomArmors);
//    }
//    // Modyfikuj customową zbroję
//    @GetMapping(path = "/rpgSystems/cpRed/customArmors/update/{armorId}")
//    public Map<String, Object> updateCustomArmor(@PathVariable("armorId") Long armorId,
//                                                 @RequestBody CpRedCustomArmors cpRedCustomArmors) {
//        return cpRedCustomArmorsService.updateCustomArmor(armorId, cpRedCustomArmors);
//    }
//
//    // ============ Admin methods ============
//    // Pobierz wszystkie customowe zbroje dla admina
//    @GetMapping(path = "/admin/rpgSystems/cpRed/customArmors/all")
//    public List<CpRedCustomArmors> getAllCustomArmorsForAdmin() {
//        return cpRedCustomArmorsService.getAllCustomArmorsForAdmin();
//    }
}

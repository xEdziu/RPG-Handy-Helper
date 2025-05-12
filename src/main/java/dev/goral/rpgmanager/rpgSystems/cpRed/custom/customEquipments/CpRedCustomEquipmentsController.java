package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customEquipments;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCustomEquipmentsController {
    private final CpRedCustomEquipmentsService cpRedCustomEquipmentsService;

//    // ============ User methods ============
//    // Pobierz wszystkie przedmioty
//    @GetMapping(path = "/rpgSystems/cpRed/customEquipments/all")
//    public Map<String, Object> getAllEquipments() { // List<CpRedCustomEquipmentsDTO>
//        return cpRedCustomEquipmentsService.getAllEquipments();
//    }
//    // Pobierz przedmiot po id
//    @GetMapping(path = "/rpgSystems/cpRed/customEquipments/{equipmentId}")
//    public Map<String, Object> getEquipmentById(@PathVariable("equipmentId") Long equipmentId) { // CpRedCustomEquipmentsDTO
//        return cpRedCustomEquipmentsService.getEquipmentById(equipmentId);
//    }
//    // Dodaj przedmiot
//    @PostMapping(path = "/rpgSystems/cpRed/customEquipments/add")
//    public Map<String, Object> addEquipment(@RequestBody CpRedCustomEquipments cpRedCustomEquipments) {
//        return cpRedCustomEquipmentsService.addEquipment(cpRedCustomEquipments);
//    }
//    // Modyfikować przedmiot
//    @PutMapping(path = "/rpgSystems/cpRed/customEquipments/update/{equipmentId}")
//    public Map<String, Object> updateEquipment(@PathVariable("equipmentId") Long equipmentId,
//                                                @RequestBody CpRedCustomEquipments cpRedCustomEquipments) {
//        return cpRedCustomEquipmentsService.updateEquipment(equipmentId, cpRedCustomEquipments);
//    }
//
//    // ============ Admin methods ============
//    // Pobierz wszystkie przedmioty dla admina
//    @GetMapping(path = "/admin/rpgSystems/cpRed/customEquipments/all")
//    public Map<String, Object> getAllEquipmentsForAdmin() { // List<CpRedCustomEquipments>
//        return cpRedCustomEquipmentsService.getAllEquipmentsForAdmin();
//    }
}

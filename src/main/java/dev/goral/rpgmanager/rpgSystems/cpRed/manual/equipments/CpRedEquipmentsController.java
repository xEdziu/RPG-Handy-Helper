package dev.goral.rpgmanager.rpgSystems.cpRed.manual.equipments;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedEquipmentsController {
    private CpRedEquipmentsService cpRedEquipmentsService;

    // ============ User methods ============
    // Pobierz wszystkie przedmioty
    @GetMapping(path = "/rpgSystems/cpRed/equipments/all")
    public Map<String, Object> getAllEquipments() { // List<CpRedEquipmentsDTO>
        return cpRedEquipmentsService.getAllEquipments();
    }
    // Pobierz przedmiot po id
    @GetMapping(path = "/rpgSystems/cpRed/equipments/{equipmentId}")
    public Map<String, Object> getEquipmentById(@PathVariable("equipmentId") Long equipmentId) { // CpRedEquipmentsDTO
        return cpRedEquipmentsService.getEquipmentById(equipmentId);
    }

    // ============ Admin methods ============
    // Pobierz wszystkie przedmioty dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/equipments/all")
    public Map<String, Object> getAllEquipmentsForAdmin() { // List<CpRedEquipments>
        return cpRedEquipmentsService.getAllEquipmentsForAdmin();
    }
    // Dodać przedmiot
    @PostMapping(path = "/admin/rpgSystems/cpRed/equipments/add")
    public Map<String, Object> addEquipment(@RequestBody CpRedEquipments cpRedEquipments) {
        return cpRedEquipmentsService.addEquipment(cpRedEquipments);
    }
    // Modyfikować przedmiot
    @PutMapping(path = "/admin/rpgSystems/cpRed/equipments/update/{equipmentId}")
    public Map<String, Object> updateEquipment(@PathVariable("equipmentId") Long equipmentId,
                                               @RequestBody CpRedEquipments cpRedEquipments) {
        return cpRedEquipmentsService.updateEquipment(equipmentId, cpRedEquipments);
    }
}

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

    @GetMapping(path = "/rpgSystems/cpRed/customEquipments/all")
    public Map<String, Object> getAllEquipments() { // List<CpRedCustomEquipmentsDTO>
        return cpRedCustomEquipmentsService.getAllEquipments();
    }

    @GetMapping(path = "/rpgSystems/cpRed/customEquipments/{equipmentId}")
    public Map<String, Object> getEquipmentById(@PathVariable("equipmentId") Long equipmentId) { // CpRedCustomEquipmentsDTO
        return cpRedCustomEquipmentsService.getEquipmentById(equipmentId);
    }

    @GetMapping(path = "/rpgSystems/cpRed/customEquipments/game/{gameId}")
    public Map<String, Object> getEquipmentsByGame(@PathVariable("gameId") Long gameId) {
        return cpRedCustomEquipmentsService.getEquipmentsByGame(gameId);
    }

    @PostMapping(path = "/rpgSystems/cpRed/customEquipments/add")
    public Map<String, Object> addEquipment(@RequestBody CpRedCustomEquipmentsRequest cpRedCustomEquipments) {
        return cpRedCustomEquipmentsService.addEquipment(cpRedCustomEquipments);
    }

    @PutMapping(path = "/rpgSystems/cpRed/customEquipments/update/{equipmentId}")
    public Map<String, Object> updateEquipment(@PathVariable("equipmentId") Long equipmentId,
                                                @RequestBody CpRedCustomEquipmentsRequest cpRedCustomEquipments) {
        return cpRedCustomEquipmentsService.updateEquipment(equipmentId, cpRedCustomEquipments);
    }

    @GetMapping(path = "/admin/rpgSystems/cpRed/customEquipments/all")
    public Map<String, Object> getAllEquipmentsForAdmin() { // List<CpRedCustomEquipments>
        return cpRedCustomEquipmentsService.getAllEquipmentsForAdmin();
    }
}

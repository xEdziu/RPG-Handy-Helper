package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEquipment;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterEquipmentController {
    private final CpRedCharacterEquipmentService cpRedCharacterEquipmentService;

    // ============ User methods ============
    @GetMapping(path = "/games/cpRed/characters/equipments/{characterId}")
    public Map<String, Object> getCharacterEquipment(@PathVariable("characterId") Long characterId) {
        return cpRedCharacterEquipmentService.getCharacterEquipment(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/equipments/create")
    public Map<String, Object> createCharacterEquipment(@RequestBody AddCharacterEquipmentRequest addCharacterEquipmentRequest) {
        return cpRedCharacterEquipmentService.createCharacterEquipment(addCharacterEquipmentRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/equipments/update/{characterEquipmentId}")
    public Map<String, Object> updateCharacterEquipment(@PathVariable("characterEquipmentId") Long characterEquipmentId,
                                                         @RequestBody UpdateCharacterEquipmentRequest updateCharacterEquipmentRequest) {
        return cpRedCharacterEquipmentService.updateCharacterEquipment(characterEquipmentId, updateCharacterEquipmentRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/equipments/delete/{characterEquipmentId}")
    public Map<String, Object> deleteCharacterEquipment(@PathVariable("characterEquipmentId") Long characterEquipmentId) {
        return cpRedCharacterEquipmentService.deleteCharacterEquipment(characterEquipmentId);
    }
    // ============ Admin methods ============
    @GetMapping(path = "/admin/games/cpRed/characters/equipments/all")
    public Map<String, Object> getAllCharacterEquipments() {
        return cpRedCharacterEquipmentService.getAllCharacterEquipments();
    }
}

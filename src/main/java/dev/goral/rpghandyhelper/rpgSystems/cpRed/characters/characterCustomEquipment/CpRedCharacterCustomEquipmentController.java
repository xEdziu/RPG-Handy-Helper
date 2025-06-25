package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomEquipment;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterCustomEquipmentController {
    private final CpRedCharacterCustomEquipmentService cpRedCharacterCustomEquipmentService;

    // ============ User methods ============
    @GetMapping(path = "/games/cpRed/characters/customEquipments/{characterId}")
    public Map<String, Object> getCharacterCustomEquipment(@PathVariable("characterId") Long characterId) {
        return cpRedCharacterCustomEquipmentService.getCharacterCustomEquipment(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/customEquipments/create")
    public Map<String, Object> createCharacterCustomEquipment(@RequestBody AddCharacterCustomEquipmentRequest addCharacterCustomEquipmentRequest) {
        return cpRedCharacterCustomEquipmentService.createCharacterCustomEquipment(addCharacterCustomEquipmentRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/customEquipments/update/{characterCustomEquipmentId}")
    public Map<String, Object> updateCharacterCustomEquipment(@PathVariable("characterCustomEquipmentId") Long characterCustomEquipmentId,
                                                        @RequestBody UpdateCharacterCustomEquipmentRequest updateCharacterCustomEquipmentRequest) {
        return cpRedCharacterCustomEquipmentService.updateCharacterCustomEquipment(characterCustomEquipmentId, updateCharacterCustomEquipmentRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/customEquipments/delete/{characterCustomEquipmentId}")
    public Map<String, Object> deleteCharacterCustomEquipment(@PathVariable("characterCustomEquipmentId") Long characterCustomEquipmentId) {
        return cpRedCharacterCustomEquipmentService.deleteCharacterCustomEquipment(characterCustomEquipmentId);
    }
    // ============ Admin methods ============
    @GetMapping(path = "/admin/games/cpRed/characters/customEquipments/all")
    public Map<String, Object> getAllCharacterCustomEquipments() {
        return cpRedCharacterCustomEquipmentService.getAllCharacterCustomEquipments();
    }
}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterAmmunitionController {
    private final CpRedCharacterAmmunitionService characterAmmunitionService;

    // ============ User methods ============
    @GetMapping(path = "/games/cpRed/characters/ammunition/{characterId}")
    public Map<String, Object> getCharacterAmmunition(@PathVariable("characterId") Long characterId) {
        return characterAmmunitionService.getCharacterAmmunition(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/ammunition/create")
    public Map<String, Object> createCharacterAmmunition(@RequestBody AddCharacterAmmunitionRequest addCharacterAmmunitionRequest) {
        return characterAmmunitionService.createCharacterAmmunition(addCharacterAmmunitionRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/ammunition/update/{characterAmmunitionId}")
    public Map<String, Object> updateCharacterAmmunition(@PathVariable("characterAmmunitionId") Long characterAmmunitionId,
                                                          @RequestBody UpdateCharacterAmmunitionRequest updateCharacterAmmunitionRequest) {
        return characterAmmunitionService.updateCharacterAmmunition(characterAmmunitionId, updateCharacterAmmunitionRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/ammunition/delete/{characterAmmunitionId}")
    public Map<String, Object> deleteCharacterAmmunition(@PathVariable("characterAmmunitionId") Long characterAmmunitionId) {
        return characterAmmunitionService.deleteCharacterAmmunition(characterAmmunitionId);
    }
    //TODO: Pamiętać że jak usuwam broń to musze też wyczyścić tę tabelę

    // ============ Admin methods ============
    @GetMapping(path = "/admin/games/cpRed/characters/ammunition/all")
    public Map<String, Object> getAllCharacterAmmunition() {
        return characterAmmunitionService.getAllCharacterAmmunition();
    }
}

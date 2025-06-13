package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCyberware;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterCyberwareController {
    private CpRedCharacterCyberwareService cpRedCharacterCyberwareService;

    // ============ User methods ============
    @GetMapping(path = "/games/cpRed/characters/cyberwares/{characterId}")
    public Map<String, Object> getCharacterCyberwares(@PathVariable Long characterId) {
        return cpRedCharacterCyberwareService.getCharacterCyberwares(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/cyberwares/create")
    public Map<String, Object> createCharacterCyberware(@RequestBody AddCharacterCyberwareRequest addCharacterCyberwareRequest) {
        return cpRedCharacterCyberwareService.createCharacterCyberware(addCharacterCyberwareRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/cyberwares/update/{characterCyberwareId}")
    public Map<String, Object> updateCharacterCyberware(@PathVariable Long characterCyberwareId,
                                                        @RequestBody UpdateCharacterCyberwareRequest updateCharacterCyberwareRequest) {
        return cpRedCharacterCyberwareService.updateCharacterCyberware(characterCyberwareId, updateCharacterCyberwareRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/cyberwares/delete/{characterCyberwareId}")
    public Map<String, Object> deleteCharacterCyberware(@PathVariable Long characterCyberwareId) {
        return cpRedCharacterCyberwareService.deleteCharacterCyberware(characterCyberwareId);
    }
    // ============ Admin methods ============
    @GetMapping(path = "/admin/games/cpRed/characters/cyberwares/all")
    public Map<String, Object> getAllCharacterCyberwares() {
        return cpRedCharacterCyberwareService.getAllCharacterCyberwares();
    }
}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterCustomCyberwareController {
    private final CpRedCharacterCustomCyberwareService cpRedCharacterCustomCyberwareService;

    @GetMapping(path="/games/cpRed/characters/customCyberwares/{characterId}")
    public Map<String,Object> getCharacterCustomCyberware(@PathVariable("characterId") Long characterId){
        return cpRedCharacterCustomCyberwareService.getCharacterCustomCyberwares(characterId);
    }

    @GetMapping(path="/admin/games/cpRed/characters/customCyberwares/all")
    public Map<String, Object> getAllCharacterCustomCyberwares() {
        return cpRedCharacterCustomCyberwareService.getAllCharacterCustomCyberwares();
    }
    @PostMapping(path = "/games/cpRed/characters/customCyberwares/create")
    public Map<String, Object> createCharacterCustomCyberware(@RequestBody AddCharacterCustomCyberwareRequest addCharacterCustomCyberwareRequest) {
        return cpRedCharacterCustomCyberwareService.createCharacterCustomCyberware(addCharacterCustomCyberwareRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/customCyberwares/update/{characterCustomCyberwareId}")
    public Map<String, Object> updateCharacterCustomCyberware(@PathVariable("characterCustomCyberwareId") Long characterCustomCyberwareId,
                                                               @RequestBody UpdateCharacterCustomCyberwareRequest updateCharacterCustomCyberwareRequest) {
        return cpRedCharacterCustomCyberwareService.updateCharacterCustomCyberware(characterCustomCyberwareId, updateCharacterCustomCyberwareRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/customCyberwares/delete/{characterCustomCyberwareId}")
    public Map<String, Object> deleteCharacterCustomCyberware(@PathVariable("characterCustomCyberwareId") Long characterCustomCyberwareId) {
        return cpRedCharacterCustomCyberwareService.deleteCharacterCustomCyberware(characterCustomCyberwareId);
    }



}

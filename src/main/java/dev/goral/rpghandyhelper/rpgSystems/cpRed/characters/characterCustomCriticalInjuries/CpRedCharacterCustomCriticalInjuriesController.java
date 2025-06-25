package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterCustomCriticalInjuriesController {
    
    private final CpRedCharacterCustomCriticalInjuriesService characterCustomCriticalInjuriesService;
    
    @GetMapping(path = "/games/cpRed/characters/customCriticalInjuries/{characterId}")
    public Map<String, Object> getCharacterCustomCriticalInjuries(@PathVariable("characterId") Long characterId) {
        return characterCustomCriticalInjuriesService.getCharacterCustomCriticalInjuries(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/customCriticalInjuries/create")
    public Map<String, Object> createCharacterCustomCriticalInjuries(@RequestBody AddCharacterCustomCriticalInjuriesRequest addCharacterCustomCriticalInjuriesRequest) {
        return characterCustomCriticalInjuriesService.createCharacterCustomCriticalInjuries(addCharacterCustomCriticalInjuriesRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/customCriticalInjuries/update/{characterCustomCriticalInjuriesId}")
    public Map<String, Object> updateCharacterCustomCriticalInjuries(@PathVariable("characterCustomCriticalInjuriesId") Long characterCustomCriticalInjuriesId,
                                                               @RequestBody UpdateCharacterCustomCriticalInjuriesRequest updateCharacterCustomCriticalInjuriesRequest) {
        return characterCustomCriticalInjuriesService.updateCharacterCustomCriticalInjuries(characterCustomCriticalInjuriesId, updateCharacterCustomCriticalInjuriesRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/customCriticalInjuries/delete/{characterCustomCriticalInjuriesId}")
    public Map<String, Object> deleteCharacterCustomCriticalInjuries(@PathVariable("characterCustomCriticalInjuriesId") Long characterCustomCriticalInjuriesId) {
        return characterCustomCriticalInjuriesService.deleteCharacterCustomCriticalInjuries(characterCustomCriticalInjuriesId);
    }

    @GetMapping(path = "/admin/games/cpRed/characters/customCriticalInjuries/all")
    public Map<String, Object> getAllCharacterCustomCriticalInjuries() {
        return characterCustomCriticalInjuriesService.getAllCharacterCustomCriticalInjuries();
    }
    
    


}

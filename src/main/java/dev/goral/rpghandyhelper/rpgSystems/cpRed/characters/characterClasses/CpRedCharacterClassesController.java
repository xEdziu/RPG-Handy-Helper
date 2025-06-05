package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterClasses;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterClassesController {
    private final CpRedCharacterClassesService cpRedCharacterClassesService;

    // ============ User methods ============
    @GetMapping(path = "/games/cpRed/characters/classes/{characterId}")
    public Map<String, Object> getCharacterClasses(@PathVariable("characterId")Long characterId) {
        return cpRedCharacterClassesService.getCharacterClasses(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/classes/create")
    public Map<String, Object> createCharacterClass(@RequestBody AddCharacterClassesRequest addCharacterClassesRequest) {
        return cpRedCharacterClassesService.createCharacterClass(addCharacterClassesRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/classes/update/{characterClassId}")
    public Map<String, Object> updateCharacterClass(@PathVariable("characterClassId") Long characterClassId, @RequestBody AddCharacterClassesRequest addCharacterClassesRequest) {
        return cpRedCharacterClassesService.updateCharacterClass(characterClassId, addCharacterClassesRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/classes/delete/{characterClassId}")
    public Map<String, Object> deleteCharacterClass(@PathVariable("characterClassId") Long characterClassId) {
        return cpRedCharacterClassesService.deleteCharacterClass(characterClassId);
    }

    // ============ Admin methods ============
    @GetMapping(path = "/admin/games/cpRed/characters/classes/all")
    public Map<String, Object> getAllCharactersClasses() {
        return cpRedCharacterClassesService.getAllCharactersClasses();
    }
}

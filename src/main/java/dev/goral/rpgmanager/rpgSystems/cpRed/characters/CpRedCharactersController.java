package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/authorized")
public class CpRedCharactersController {

    private final CpRedCharactersService cpRedCharactersService;

    @GetMapping(path = "/games/cpRed/characters/all")
    public Map<String, Object> getAllCharacters() {
        return cpRedCharactersService.getAllCharacters();
    }

    @GetMapping(path = "/games/cpRed/characters/{characterId}")
    public Map<String, Object> getCharacter(@PathVariable("characterId") Long characterId) {
        return cpRedCharactersService.getCharacter(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/create")
    public Map<String, Object> createCharacter(@RequestBody CpRedCharacters character) {
        return cpRedCharactersService.createCharacter(character);
    }

    @PutMapping(path = "/games/cpRed/characters/update/{characterId}")
    public Map<String, Object> updateCharacter(@PathVariable("characterId") Long characterId, @RequestBody CpRedCharacters character) {
        return cpRedCharactersService.updateCharacter(characterId, character);
    }

    @PutMapping(path = "/games/cpRed/characters/playerToNpc/{characterId}")
    public Map<String, Object> playerToNpc(@PathVariable("characterId") Long characterId) {
        return cpRedCharactersService.playerToNpc(characterId);
    }


    //TODO: Dodać opcje kopiowania postaci razem ze statami

}

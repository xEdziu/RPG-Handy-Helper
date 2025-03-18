package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/authorized")
public class CpRedCharactersController {

    private final CpRedCharactersService cpRedCharactersService;

    @GetMapping(path = "/games/cpRed/characters/all")
    public List<CpRedCharactersDTO> getAllCharacters() {
        return cpRedCharactersService.getAllCharacters();
    }

    @GetMapping(path = "/games/cpRed/characters/{characterId}")
    public CpRedCharactersDTO getCharacter(@PathVariable("characterId") Long characterId) {
        return cpRedCharactersService.getCharacter(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/create")
    public Map<String, Object> createCharacter(@RequestBody CpRedCharacters character) {
        return cpRedCharactersService.createCharacter(character);
    }

    @PutMapping(path = "games/cpRed/characters/update/{characterId}")
    public Map<String, Object> updateCharacter(@PathVariable("characterId") Long characterId, @RequestBody CpRedCharacters character) {
        return cpRedCharactersService.updateCharacter(characterId, character);
    }

    //TODO: Dodać opcje kopiowania postaci razem ze statami

}

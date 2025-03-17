package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/authorized")
public class CpRedCharactersController {

    private final CpRedCharactersService cpRedCharactersService;

    @Autowired
    public CpRedCharactersController(CpRedCharactersService cpRedCharactersService) {
        this.cpRedCharactersService = cpRedCharactersService;
    }

    @GetMapping(path = "/cpRed/characters/all")
    public List<CpRedCharactersDTO> getAllCharacters() {
        return cpRedCharactersService.getAllCharacters();
    }

    @GetMapping(path = "/cpRed/characters/{characterId}")
    public CpRedCharactersDTO getCharacter(@PathVariable("characterId") Long characterId) {
        return cpRedCharactersService.getCharacter(characterId);
    }

    @PostMapping(path = "/cpRed/characters/create")
    public Map<String, Object> createCharacter(@RequestBody CpRedCharacters character) {
        return cpRedCharactersService.createCharacter(character);
    }

    @PutMapping(path = "/cpRed/characters/update/{characterId}")
    public Map<String, Object> updateCharacter(@PathVariable("characterId") Long characterId, @RequestBody CpRedCharacters character) {
        return cpRedCharactersService.updateCharacter(characterId, character);
    }

    //TODO: Dodać opcje kopiowania postaci razem ze statami

}

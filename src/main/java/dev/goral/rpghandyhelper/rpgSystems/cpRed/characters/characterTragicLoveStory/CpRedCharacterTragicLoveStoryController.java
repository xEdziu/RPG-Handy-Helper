package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterTragicLoveStory;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterTragicLoveStory.CpRedCharacterTragicLoveStoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterTragicLoveStoryController {
    private final CpRedCharacterTragicLoveStoryService cpRedCharacterTragicLoveStoryService;

    @GetMapping(path = "/rpgSystems/cpRed/character/tragicLoveStory/all")
    public Map<String, Object> getAllTragicLoveStory() {
        return cpRedCharacterTragicLoveStoryService.getAllTragicLoveStory();
    }

    @GetMapping(path = "/rpgSystems/cpRed/character/tragicLoveStory/{storyId}")
    public Map<String, Object> getTragicLoveStoryById(@PathVariable("storyId") Long storyId) {
        return cpRedCharacterTragicLoveStoryService.getTragicLoveStoryById(storyId);
    }

    @GetMapping(path = "/rpgSystems/cpRed/character/tragicLoveStory/character/{characterId}")
    public Map<String, Object> getTragicLoveStoryByCharacterId(@PathVariable("characterId") Long characterId) {
        return cpRedCharacterTragicLoveStoryService.getTragicLoveStoryByCharacterId(characterId);
    }

    @GetMapping(path = "/admin/rpgSystems/cpRed/character/tragicLoveStory/all")
    public Map<String, Object> getAllTragicLoveStoryForAdmin() {
        return cpRedCharacterTragicLoveStoryService.getAllTragicLoveStoryForAdmin();
    }

    @PostMapping(path = "/rpgSystems/cpRed/character/tragicLoveStory/add")
    public Map<String, Object> addStory(@RequestBody CpRedCharacterTragicLoveStoryRequest cpRedCharacterTragicLoveStory) {
        return cpRedCharacterTragicLoveStoryService.addTragicLoveStory(cpRedCharacterTragicLoveStory);
    }

    @DeleteMapping(path = "/rpgSystems/cpRed/character/tragicLoveStory/delete/{storyId}")
    public Map<String, Object> deleteStory(@PathVariable("storyId") Long storyId) {
        return cpRedCharacterTragicLoveStoryService.deleteTragicLoveStory(storyId);
    }

    @PutMapping(path = "/rpgSystems/cpRed/character/tragicLoveStory/update/{storyId}")
    public Map<String, Object> updateStory(@PathVariable("storyId") Long storyId,
                                           @RequestBody CpRedCharacterTragicLoveStoryRequest cpRedCharacterTragicLoveStory) {
        return cpRedCharacterTragicLoveStoryService.updateTragicLoveStory(storyId, cpRedCharacterTragicLoveStory);
    }
}

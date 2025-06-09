package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterStats;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterStatsController {
    private final CpRedCharacterStatsService cpRedCharacterStatsService;

    // ============ User methods ============
    @GetMapping(path ="/games/cpRed/characters/stats/{characterId}")
    public Map<String, Object> getCharacterStats(@PathVariable("characterId")Long characterId) {
        return cpRedCharacterStatsService.getCharacterStats(characterId);
    }

    @PostMapping(path = "/games/cpRed/characters/stats/create")
    public Map<String, Object> createCharacterStats(@RequestBody AddCharacterStatsRequest addCharacterStatsRequest) {
        return cpRedCharacterStatsService.createCharacterStats(addCharacterStatsRequest);
    }

    @PutMapping(path = "/games/cpRed/characters/stats/update/{characterStatsId}")
    public Map<String, Object> updateCharacterStats(@PathVariable("characterStatsId") Long characterStatsId,
                                                    @RequestBody UpdateCharacterStatsRequest updateCharacterStatsRequest) {
        return cpRedCharacterStatsService.updateCharacterStats(characterStatsId, updateCharacterStatsRequest);
    }

    @DeleteMapping(path = "/games/cpRed/characters/stats/delete/{characterStatsId}")
    public Map<String, Object> deleteCharacterStats(@PathVariable("characterStatsId") Long characterStatsId) {
        return cpRedCharacterStatsService.deleteCharacterStats(characterStatsId);
    }

    // ============ Admin methods ============
    @GetMapping(path = "/admin/games/cpRed/characters/stats/all")
    public Map<String, Object> getAllCharacterStats() {
        return cpRedCharacterStatsService.getAllCharacterStats();
    }
}

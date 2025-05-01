package dev.goral.rpgmanager.rpgSystems.cpRed.manual.stats;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedStatsController {
    private final CpRedStatsService cpRedStatsService;

    // ============ User methods ============
    // Pobierz wszystkie statystyki
    @GetMapping(path = "/rpgSystems/cpRed/stats/all")
    public List<CpRedStatsDTO> getAllStats() {
        return cpRedStatsService.getAllStats();
    }
    // Pobierz statystykę po id
    @GetMapping(path = "/rpgSystems/cpRed/stats/{statId}")
    public CpRedStatsDTO getStatById(@PathVariable("statId") Long statId) {
        return cpRedStatsService.getStatById(statId);
    }


    // ============ Admin methods ============
    // Pobierz wszystkie statystyki dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/stats/all")
    public List<CpRedStats> getAllStatsForAdmin() {
        return cpRedStatsService.getAllStatsForAdmin();
    }
    // Dodać statystykę
    @PostMapping(path = "/admin/rpgSystems/cpRed/stats/add")
    public Map<String, Object> addStat(CpRedStats cpRedStats) {
        return cpRedStatsService.addStat(cpRedStats);
    }
    // Modyfikować statystykę
    @PutMapping(path = "/admin/rpgSystems/cpRed/stats/update/{statId}")
    public Map<String, Object> updateStat(@PathVariable("statId") Long statId,
                                          @RequestBody CpRedStats cpRedStats) {
        return cpRedStatsService.updateStat(statId, cpRedStats);
    }
}

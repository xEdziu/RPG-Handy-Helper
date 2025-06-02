package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.stats;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedStatsController {
    private final CpRedStatsService cpRedStatsService;

    // ============ User methods ============
    // Pobierz wszystkie statystyki
    @GetMapping(path = "/rpgSystems/cpRed/stats/all")
    public Map<String, Object> getAllStats() { // List<CpRedStatsDTO>
        return cpRedStatsService.getAllStats();
    }
    // Pobierz statystykę po id
    @GetMapping(path = "/rpgSystems/cpRed/stats/{statId}")
    public Map<String, Object> getStatById(@PathVariable("statId") Long statId) { // CpRedStatsDTO
        return cpRedStatsService.getStatById(statId);
    }


    // ============ Admin methods ============
    // Pobierz wszystkie statystyki dla admina
    @GetMapping(path = "/admin/rpgSystems/cpRed/stats/all")
    public Map<String, Object> getAllStatsForAdmin() { // List<CpRedStats>
        return cpRedStatsService.getAllStatsForAdmin();
    }
    // Dodać statystykę
    @PostMapping(path = "/admin/rpgSystems/cpRed/stats/add")
    public Map<String, Object> addStat(@RequestBody CpRedStats cpRedStats) {
        return cpRedStatsService.addStat(cpRedStats);
    }
    // Modyfikować statystykę
    @PutMapping(path = "/admin/rpgSystems/cpRed/stats/update/{statId}")
    public Map<String, Object> updateStat(@PathVariable("statId") Long statId,
                                          @RequestBody CpRedStats cpRedStats) {
        return cpRedStatsService.updateStat(statId, cpRedStats);
    }
    // Zmienić zmienność statystyki
    @PutMapping(path = "/admin/rpgSystems/cpRed/stats/changeable/{statId}")
    public Map<String, Object> changeStatChangeable(@PathVariable("statId") Long statId) {
        return cpRedStatsService.changeStatChangeable(statId);
    }
}

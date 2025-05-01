package dev.goral.rpgmanager.rpgSystems.cpRed.manual.stats;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedStatsService {
    private final CpRedStatsRepository cpRedStatsRepository;

//    // Pobierz wszystkie statystyki
//    public List<CpRedStatsDTO> getAllStats() {
//
//    }
//
//    // Pobierz statystyke po id
//    public CpRedStatsDTO getStatById(Long id) {
//
//    }
//
//    // Pobierz wszystkie statystyki dla admina
//    public List<CpRedStats> getAllStatsForAdmin() {
//        return cpRedStatsRepository.findAll();
//    }
//
//    // Dodać statystyke
//    public Map<String, Object> addStat(CpRedStats cpRedStats) {
//
//    }
//
//    // Modyfikować statystyke
//    public Map<String, Object> updateStat(Long id, CpRedStats cpRedStats) {
//
//    }
}

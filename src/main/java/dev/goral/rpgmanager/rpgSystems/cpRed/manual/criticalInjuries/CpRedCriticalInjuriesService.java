package dev.goral.rpgmanager.rpgSystems.cpRed.manual.criticalInjuries;

import dev.goral.rpgmanager.security.CustomReturnables;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCriticalInjuriesService {
    private final CpRedCriticalInjuriesRepository cpRedCriticalInjuriesRepository;

    // Pobierz wszystkie obrażenia krytyczne
    public Map<String, Object> getAllCriticalInjuries() {
        List<CpRedCriticalInjuriesDTO> cpRedCriticalInjuriesList = cpRedCriticalInjuriesRepository.findAll().stream().
                map(cpRedCriticalInjuries -> new CpRedCriticalInjuriesDTO(
                        cpRedCriticalInjuries.getRollValue(),
                        cpRedCriticalInjuries.getInjuryPlace().toString(),
                        cpRedCriticalInjuries.getName(),
                        cpRedCriticalInjuries.getEffects(),
                        cpRedCriticalInjuries.getPatching(),
                        cpRedCriticalInjuries.getTreating()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wszystkie rodzaje obrażeń krytycznych.");
        response.put("criticalInjuries", cpRedCriticalInjuriesList);
        return response;

    }
//
    // Pobierz obrażenia krytyczne po id
    public Map<String, Object> getCriticalInjuryById(Long criticalInjuryId) {
        CpRedCriticalInjuriesDTO criticalInjury = cpRedCriticalInjuriesRepository.findById(criticalInjuryId).map(
                cpRedCriticalInjuries -> new CpRedCriticalInjuriesDTO(
                        cpRedCriticalInjuries.getRollValue(),
                        cpRedCriticalInjuries.getInjuryPlace().toString(),
                        cpRedCriticalInjuries.getName(),
                        cpRedCriticalInjuries.getEffects(),
                        cpRedCriticalInjuries.getPatching(),
                        cpRedCriticalInjuries.getTreating()
                )).orElseThrow(() -> new RuntimeException("Obrażenia krytyczne o id " + criticalInjuryId + " nie istnieją"));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano obrażenia krytyczne o id " + criticalInjuryId);
        response.put("criticalInjury", criticalInjury);
        return response;

    }

    // Pobierz wszystkie obrażenia krytyczne dla admina
    public Map<String, Object> getAllCriticalInjuriesForAdmin() {
        List<CpRedCriticalInjuries> allCriticalInjuries = cpRedCriticalInjuriesRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wszystkie obrażenia krytyczne.");
        response.put("criticalInjuries", allCriticalInjuries);
        return response;

    }
//
//    // Dodaj obrażenia krytyczne
//    public Map<String, Object> addCriticalInjury(CpRedCriticalInjuries cpRedCriticalInjuries) {
//
//    }
//
//    // Modyfikuj obrażenia krytyczne
//    public Map<String, Object> updateCriticalInjury(Long criticalInjuryId, CpRedCriticalInjuries cpRedCriticalInjuries) {
//
//    }

}

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
//    // Pobierz obrażenia krytyczne po id
//    public Map<String, Object> getCriticalInjuryById(Long criticalInjuryId) {
//
//    }
//
//    // Pobierz wszystkie obrażenia krytyczne dla admina
//    public Map<String, Object> getAllCriticalInjuriesForAdmin() {
//
//    }
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

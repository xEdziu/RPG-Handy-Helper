package dev.goral.rpgmanager.rpgSystems.cpRed.manual.criticalInjuries;

import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import dev.goral.rpgmanager.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCriticalInjuriesService {
    private final CpRedCriticalInjuriesRepository cpRedCriticalInjuriesRepository;
    private final UserRepository userRepository;

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

    public Map<String, Object> getAllCriticalInjuriesForAdmin() {
        List<CpRedCriticalInjuries> allCriticalInjuries = cpRedCriticalInjuriesRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wszystkie obrażenia krytyczne.");
        response.put("criticalInjuries", allCriticalInjuries);
        return response;

    }

    public Map<String, Object> addCriticalInjury(CpRedCriticalInjuries cpRedCriticalInjuries) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do dodawania obrażeń krytycznych.");
        }
        if(cpRedCriticalInjuries.getInjuryPlace().toString()==null ||
                cpRedCriticalInjuries.getName()==null ||
                cpRedCriticalInjuries.getEffects()==null ||
                cpRedCriticalInjuries.getPatching()==null ||
                cpRedCriticalInjuries.getTreating()==null) {
            throw new IllegalStateException("Nie wszystkie pola zostały wypełnione.");
        }
        if (cpRedCriticalInjuries.getRollValue() <= 0) {
            throw new IllegalStateException("Rzut nie może być ujemny lub równy zero.");
        }
        if(cpRedCriticalInjuriesRepository.existsByName(cpRedCriticalInjuries.getName())) {
            throw new IllegalStateException("Obrażenia krytyczne o tej nazwie już istnieją.");
        }
        if (cpRedCriticalInjuries.getName().isEmpty() || cpRedCriticalInjuries.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa obrażeń krytycznych nie może być pusta.");
        }
        if (cpRedCriticalInjuries.getName().length() > 255) {
            throw new IllegalStateException("Nazwa obrażeń krytycznych nie może być dłuższa niż 255 znaków.");
        }
        if (cpRedCriticalInjuries.getEffects().isEmpty() || cpRedCriticalInjuries.getEffects().trim().isEmpty()) {
            throw new IllegalStateException("Efekty obrażeń krytycznych nie mogą być puste.");
        }
        if (cpRedCriticalInjuries.getEffects().length() > 500) {
            throw new IllegalStateException("Efekty obrażeń krytycznych nie mogą być dłuższe niż 500 znaków.");
        }

        if (cpRedCriticalInjuries.getPatching().isEmpty() || cpRedCriticalInjuries.getPatching().trim().isEmpty()) {
            throw new IllegalStateException("Łatanie obrażeń krytycznych nie moźe być puste.");
        }
        if (cpRedCriticalInjuries.getPatching().length() > 255) {
            throw new IllegalStateException("Łatanie obrażeń krytycznych nie może być dłuższe niż 255 znaków.");
        }
        if (cpRedCriticalInjuries.getTreating().isEmpty() || cpRedCriticalInjuries.getTreating().trim().isEmpty()) {
            throw new IllegalStateException("Leczenie obrażeń krytycznych nie moźe być puste.");
        }
        if (cpRedCriticalInjuries.getTreating().length() > 255) {
            throw new IllegalStateException("Leczenie obrażeń krytycznych nie może być dłuższe niż 255 znaków.");
        }
        CpRedCriticalInjuries newCriticalInjury = new CpRedCriticalInjuries(
                null,
                cpRedCriticalInjuries.getRollValue(),
                cpRedCriticalInjuries.getInjuryPlace(),
                cpRedCriticalInjuries.getName(),
                cpRedCriticalInjuries.getEffects(),
                cpRedCriticalInjuries.getPatching(),
                cpRedCriticalInjuries.getTreating()
        );
        cpRedCriticalInjuriesRepository.save(newCriticalInjury);
        return CustomReturnables.getOkResponseMap("Obrażenia krytyczne zostały dodane.");
    }

//    public Map<String, Object> updateCriticalInjury(Long criticalInjuryId, CpRedCriticalInjuries cpRedCriticalInjuries) {
//
//    }

}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEnemies;

import dev.goral.rpghandyhelper.security.CustomReturnables;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCharacterEnemiesService {
    private final CpRedCharacterEnemiesRepository CpRedCharacterEnemiesRepository;


    public Map<String, Object> getAllEnemies() {
        List<CpRedCharacterEnemies> enemies = CpRedCharacterEnemiesRepository.findAll();
        List<CpRedCharacterEnemiesDTO> enemiesDTO = enemies.stream().map(enemy ->
                new CpRedCharacterEnemiesDTO(
                        enemy.getCharacterId().getId(),
                        enemy.getName(),
                        enemy.getWhoIs(),
                        enemy.getCauseOfConflict(),
                        enemy.getWhatHas(),
                        enemy.getIntends(),
                        enemy.getDescription()
                )).toList();
        if (enemiesDTO.isEmpty()) {
            return CustomReturnables.getOkResponseMap("Brak wrogów");
        }
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wrogów");
        response.put("enemies", enemiesDTO);
        return response;
    }
}

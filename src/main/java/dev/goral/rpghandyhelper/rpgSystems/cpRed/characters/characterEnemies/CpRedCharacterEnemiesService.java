package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEnemies;

import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
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

    public  Map<String, Object> getEnemyById(Long enemyId) {
        CpRedCharacterEnemiesDTO enemy = CpRedCharacterEnemiesRepository.findById(enemyId)
                .map(cpRedCharacterEnemies -> new CpRedCharacterEnemiesDTO(
                        cpRedCharacterEnemies.getCharacterId().getId(),
                        cpRedCharacterEnemies.getName(),
                        cpRedCharacterEnemies.getWhoIs(),
                        cpRedCharacterEnemies.getCauseOfConflict(),
                        cpRedCharacterEnemies.getWhatHas(),
                        cpRedCharacterEnemies.getIntends(),
                        cpRedCharacterEnemies.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Wróg o id " + enemyId + " nie został znaleziony"));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wroga");
        response.put("enemy", enemy);
        return response;
    }

    public Map<String, Object> getEnemiesByCharacterId(Long characterId) {
        List<CpRedCharacterEnemies> enemies = CpRedCharacterEnemiesRepository.findAllByCharacterId_Id(characterId);
        if (enemies.isEmpty()) {
            return CustomReturnables.getOkResponseMap("Brak wrogów dla postaci o id " + characterId);
        }
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
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wrogów dla postaci o id " + characterId);
        response.put("enemies", enemiesDTO);
        return response;
    }

}

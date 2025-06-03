package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEnemies;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCharacterEnemiesService {
    private final CpRedCharacterEnemiesRepository CpRedCharacterEnemiesRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private  final GameUsersRepository gameUsersRepository;


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

    public Map<String, Object> getAllEnemiesForAdmin() {
        List<CpRedCharacterEnemies> allEnemies = CpRedCharacterEnemiesRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wrogów");
        response.put("enemies", allEnemies);
        return response;
    }

    public Map<String,Object> addEnemy(CpRedCharacterEnemies cpRedCharacterEnemies) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        CpRedCharacters character = cpRedCharactersRepository.findById(cpRedCharacterEnemies.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + cpRedCharacterEnemies.getCharacterId().getId() + " nie została znaleziona"));

        GameUsers gameUsers = gameUsersRepository.findByUserId(currentUser.getId());
        if (gameUsers == null) {
            throw new ResourceNotFoundException("Nie znaleziono użytkownika w grze.");
        }
        if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER || cpRedCharactersRepository.) {
            throw new IllegalStateException("Nie masz uprawnień do dodawania wrogów.");
        }



        cpRedCharacterEnemies.setCharacterId(character);


        }
    }

}

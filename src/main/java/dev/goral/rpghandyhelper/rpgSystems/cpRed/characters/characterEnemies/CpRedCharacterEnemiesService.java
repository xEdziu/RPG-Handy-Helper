package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEnemies;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameStatus;
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

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCharacterEnemiesService {
    private final CpRedCharacterEnemiesRepository CpRedCharacterEnemiesRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private  final GameUsersRepository gameUsersRepository;
    private final CpRedCharacterEnemiesRepository cpRedCharacterEnemiesRepository;


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
            return CustomReturnables.getOkResponseMap("Brak wrogów dla tej postaci.");
        }
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wrogów dla tej postaci.");
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
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wroga dla tej postaci.");
        response.put("enemy", enemy);
        return response;
    }

    public Map<String, Object> getEnemiesByCharacterId(Long characterId) {
        List<CpRedCharacterEnemies> enemies = CpRedCharacterEnemiesRepository.findAllByCharacterId(characterId);
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
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano wrogów dla tej postaci.");
        response.put("enemies", allEnemies);
        return response;
    }

    public Map<String,Object> addEnemy(CpRedCharacterEnemiesRequest cpRedCharacterEnemies) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if(cpRedCharacterEnemies.getName() == null ||
            cpRedCharacterEnemies.getWhoIs() == null ||
            cpRedCharacterEnemies.getCauseOfConflict() == null ||
            cpRedCharacterEnemies.getWhatHas() == null ||
            cpRedCharacterEnemies.getIntends() == null ||
            cpRedCharacterEnemies.getDescription() == null
        ) {
            throw new IllegalArgumentException("Wszystkie pola są wymagane.");
        }

        CpRedCharacters character = cpRedCharactersRepository.findById(cpRedCharacterEnemies.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + cpRedCharacterEnemies.getCharacterId() + " nie została znaleziona"));

        Game game=character.getGame();
        if (game == null) {
            throw new ResourceNotFoundException("Gra dla tej postaci nie została znaleziona.");
        }

        if (game.getStatus()!= GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra nie jest aktywna.");
        }

        GameUsers gameUsers = gameUsersRepository.findByGameIdAndUserId(character.getGame().getId(), currentUser.getId());
        if (gameUsers == null) {
            throw new ResourceNotFoundException("Nie znaleziono użytkownika w grze.");
        }

        if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do dodawania wrogów dla tej postaci.");
            }
        }

        if( CpRedCharacterEnemiesRepository.existsByNameAndCharacterId(cpRedCharacterEnemies.getName(), character)) {
            throw new IllegalArgumentException("Wróg o tej nazwie już istnieje dla tej postaci.");
        }
        if(cpRedCharacterEnemies.getName().isEmpty()||
                cpRedCharacterEnemies.getName().trim().isEmpty()){
            throw new IllegalArgumentException("Nazwa tego wroga nie może być pusta dla tej postaci..");
        }
        if(cpRedCharacterEnemies.getName().length()>255){
            throw new IllegalArgumentException("Nazwa tego wroga nie może być dłuższa niż 255 znaków dla tej postaci.");
        }
        if(cpRedCharacterEnemies.getWhoIs().length()>255){
            throw new IllegalArgumentException("Pochodzenie tego wroga nie może być dłuższy niż 500 znaków dla tej postaci.");
        }
        if(cpRedCharacterEnemies.getCauseOfConflict().length()>255) {
            throw new IllegalArgumentException("Przyczyna konfliktu tego wroga nie może być dłuższa niż 255 znaków dla tej postaci.");
        }
        if(cpRedCharacterEnemies.getWhatHas().length()>255) {
            throw new IllegalArgumentException("To co posiada ten wróg nie może być dłuższe niż 255 znaków dla tej postaci.");
        }
        if(cpRedCharacterEnemies.getIntends().length()>255) {
            throw new IllegalArgumentException("Zamiary tego wroga nie mogą być dłuższe niż 255 znaków dla tej postaci.");
        }
        if(cpRedCharacterEnemies.getDescription().length()>500) {
            throw new IllegalArgumentException("Opis tego wroga nie może być dłuższy niż 500 znaków dla tej postaci.");
        }
        CpRedCharacterEnemies newEnemy = new CpRedCharacterEnemies(
                null,
                character,
                cpRedCharacterEnemies.getName(),
                cpRedCharacterEnemies.getWhoIs(),
                cpRedCharacterEnemies.getCauseOfConflict(),
                cpRedCharacterEnemies.getWhatHas(),
                cpRedCharacterEnemies.getIntends(),
                cpRedCharacterEnemies.getDescription()
        );
        cpRedCharacterEnemiesRepository.save(newEnemy);
        return CustomReturnables.getOkResponseMap("Wróg został dodany.");
    }
    public Map<String, Object> deleteEnemy(Long enemyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterEnemies enemy = cpRedCharacterEnemiesRepository.findById(enemyId)
                .orElseThrow(() -> new ResourceNotFoundException("Wróg o id " + enemyId + " nie został znaleziony"));

        CpRedCharacters character = cpRedCharactersRepository.findById(enemy.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + enemy.getCharacterId().getId() + " nie została znaleziona"));

        Game game=character.getGame();
        if (game == null) {
            throw new ResourceNotFoundException("Gra dla tej postaci nie została znaleziona.");
        }

        if (game.getStatus()!= GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra nie jest aktywna.");
        }
        GameUsers gameUsers = gameUsersRepository.findByUserId(currentUser.getId());
        if (gameUsers == null) {
            throw new ResourceNotFoundException("Nie znaleziono użytkownika w grze.");
        }

        if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do usuwania wrogów dla tej postaci.");
            }
        }
        cpRedCharacterEnemiesRepository.deleteById(enemyId);
        return CustomReturnables.getOkResponseMap("Wróg o id " + enemyId + " został usunięty.");
    }

    public Map<String, Object> updateEnemy(Long enemyId, CpRedCharacterEnemiesRequest cpRedCharacterEnemies) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterEnemies enemyToUpdate = cpRedCharacterEnemiesRepository.findById(enemyId)
                .orElseThrow(() -> new ResourceNotFoundException("Wróg o id " + enemyId + " nie został znaleziony"));

        CpRedCharacters character = cpRedCharactersRepository.findById(enemyToUpdate.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + enemyToUpdate.getCharacterId().getId() + " nie została znaleziona"));

        Game game=character.getGame();
        if (game == null) {
            throw new ResourceNotFoundException("Gra dla tej postaci nie została znaleziona.");
        }

        if (game.getStatus()!= GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra nie jest aktywna.");
        }

        GameUsers gameUsers = gameUsersRepository.findByGameIdAndUserId(character.getGame().getId(), currentUser.getId());
        if (gameUsers == null) {
            throw new ResourceNotFoundException("Nie znaleziono użytkownika w grze.");
        }

        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do modyfikacji wrogów dla tej postaci.");
            }
        }

        if (enemyToUpdate.getCharacterId().getId() != cpRedCharacterEnemies.getCharacterId()) {
            throw new IllegalArgumentException("Nie można zmienić postaci tego wroga.");
        }
        if (cpRedCharacterEnemies.getName() != null) {
            if (cpRedCharacterEnemiesRepository.existsByNameAndCharacterId(cpRedCharacterEnemies.getName(), character)) {
                throw new IllegalArgumentException("Wróg o tej nazwie już istnieje dla tej postaci.");
            }
            if (cpRedCharacterEnemies.getName().isEmpty() ||
                    cpRedCharacterEnemies.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Nazwa tego wroga nie może być pusta dla tej postaci.");
            }
            if (cpRedCharacterEnemies.getName().length() > 255) {
                throw new IllegalArgumentException("Nazwa tego wroga nie może być dłuższa niż 255 znaków dla tej postaci.");
            }
            enemyToUpdate.setName(cpRedCharacterEnemies.getName());
        }
        if (cpRedCharacterEnemies.getWhoIs() != null) {
            if (cpRedCharacterEnemies.getWhoIs().length() > 255) {
                throw new IllegalArgumentException("Pochodzenie tego wroga nie może być dłuższe niż 255 znakówdla tej postaci.");
            }
            enemyToUpdate.setWhoIs(cpRedCharacterEnemies.getWhoIs());
        }
        if (cpRedCharacterEnemies.getCauseOfConflict() != null) {
            if (cpRedCharacterEnemies.getCauseOfConflict().length() > 255) {
                throw new IllegalArgumentException("Przyczyna konfliktu tego wroga nie może być dłuższa niż 255 znaków dla tej postaci.");
            }
            enemyToUpdate.setCauseOfConflict(cpRedCharacterEnemies.getCauseOfConflict());
        }
        if (cpRedCharacterEnemies.getWhatHas() != null) {
            if (cpRedCharacterEnemies.getWhatHas().length() > 255) {
                throw new IllegalArgumentException("To co posiada ten wróg nie może być dłuższe niż 255 znaków dla tej postaci.");
            }
            enemyToUpdate.setWhatHas(cpRedCharacterEnemies.getWhatHas());
        }
        if (cpRedCharacterEnemies.getIntends() != null) {
            if (cpRedCharacterEnemies.getIntends().length() > 255) {
                throw new IllegalArgumentException("Zamiary tego wroga nie mogą być dłuższe niż 255 znaków dla tej postaci.");
            }
            enemyToUpdate.setIntends(cpRedCharacterEnemies.getIntends());
        }
        if (cpRedCharacterEnemies.getDescription() != null) {
            if (cpRedCharacterEnemies.getDescription().length() > 500) {
                throw new IllegalArgumentException("Opis tego wroga nie może być dłuższy niż 500 znaków dla tej postaci.");
            }
            enemyToUpdate.setDescription(cpRedCharacterEnemies.getDescription());
        }
        cpRedCharacterEnemiesRepository.save(enemyToUpdate);
        return CustomReturnables.getOkResponseMap("Wrog został zaktualizowany.");
    }
}

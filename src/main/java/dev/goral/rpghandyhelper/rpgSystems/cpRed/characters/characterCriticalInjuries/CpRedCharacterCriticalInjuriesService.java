package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries.CpRedCharacterCriticalInjuries;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries.CpRedCharacterCriticalInjuriesRequest;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.criticalInjuries.CpRedCriticalInjuries;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.criticalInjuries.CpRedCriticalInjuriesRepository;
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
import java.util.Objects;

@Service
@AllArgsConstructor
public class CpRedCharacterCriticalInjuriesService {
    private final dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries.CpRedCharacterCriticalInjuriesRepository CpRedCharacterCriticalInjuriesRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private  final GameUsersRepository gameUsersRepository;
    private final CpRedCharacterCriticalInjuriesRepository cpRedCharacterCriticalInjuriesRepository;
    private final CpRedCriticalInjuriesRepository cpRedCriticalInjuriesRepository;

    public Map<String, Object> getAllCriticalInjuries() {
        List<CpRedCharacterCriticalInjuries> injuries = CpRedCharacterCriticalInjuriesRepository.findAll();
        List<CpRedCharacterCriticalInjuriesDTO> injuriesDTO = injuries.stream().map(injury ->
                new CpRedCharacterCriticalInjuriesDTO(
                        injury.getId(),
                        injury.getStatus().toString(),
                        injury.getCharacterId().getId(),
                        injury.getInjuriesId().getId()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano rany krytyczne dla postaci.");
        response.put("injuries", injuriesDTO);
        return response;
    }
    public  Map<String, Object> getCriticalInjuryById(Long characterInjuryId) {
        CpRedCharacterCriticalInjuriesDTO injury = CpRedCharacterCriticalInjuriesRepository.findById(characterInjuryId)
                .map(cpRedCharacterCriticalInjuries -> new CpRedCharacterCriticalInjuriesDTO(
                        cpRedCharacterCriticalInjuries.getId(),
                        cpRedCharacterCriticalInjuries.getStatus().toString(),
                        cpRedCharacterCriticalInjuries.getCharacterId().getId(),
                        cpRedCharacterCriticalInjuries.getInjuriesId().getId()
                )).orElseThrow(() -> new ResourceNotFoundException("Rana krytyczna o id " + characterInjuryId + " nie została znaleziona"));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano ranę krytyczną dla postaci.");
        response.put("injury", injury);
        return response;
    }

    public Map<String, Object> getCriticalInjuriesByCharacterId(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterCriticalInjuriesDTO> injuriesDTO = cpRedCharacterCriticalInjuriesRepository.findAllByCharacterId(character).stream().map(injury ->
                new CpRedCharacterCriticalInjuriesDTO(
                        injury.getId(),
                        injury.getStatus().toString(),
                        injury.getCharacterId().getId(),
                        injury.getInjuriesId().getId()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano rany krytyczne w dla postaci o id " + characterId);
        response.put("injuries", injuriesDTO);
        return response;
    }
    public Map<String, Object> getAllCriticalInjuriesForAdmin() {
        List<CpRedCharacterCriticalInjuries> allInjuries = CpRedCharacterCriticalInjuriesRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano rany krytyczne w dla tej postaci.");
        response.put("injuries", allInjuries);
        return response;
    }

    public Map<String,Object> addInjury(CpRedCharacterCriticalInjuriesRequest cpRedCharacterCriticalInjuriesRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if(cpRedCharacterCriticalInjuriesRequest.getCharacterId() == null ||
                cpRedCharacterCriticalInjuriesRequest.getInjuriesId() == null||
                cpRedCharacterCriticalInjuriesRequest.getStatus() == null) {
            throw new IllegalArgumentException("Wszystkie pola są wymagane.");
        }

        CpRedCharacters character = cpRedCharactersRepository.findById(cpRedCharacterCriticalInjuriesRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + cpRedCharacterCriticalInjuriesRequest.getCharacterId() + " nie została znaleziona"));

        CpRedCriticalInjuries injuries = cpRedCriticalInjuriesRepository.findById(cpRedCharacterCriticalInjuriesRequest.getInjuriesId())
                .orElseThrow(() -> new ResourceNotFoundException("Rana krytyczna o id " + cpRedCharacterCriticalInjuriesRequest.getInjuriesId() + " nie została znaleziona"));

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

        if(character.getUser()==null){
            if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
                throw new IllegalStateException("Nie masz uprawnień do dodawania ran krytycznych dla tej postaci.");
            }
        }
        else if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do dodawania ran krytycznych dla tej postaci.");
            }
        }

        if( CpRedCharacterCriticalInjuriesRepository.existsByInjuriesId_IdAndCharacterId(cpRedCharacterCriticalInjuriesRequest.getInjuriesId(), character)) {
            throw new IllegalArgumentException("Rana krytyczna o tym Id już istnieje dla tej postaci.");
        }

        CpRedCharacterCriticalInjuries newInjury = new CpRedCharacterCriticalInjuries(
                null,
                character,
                injuries,
                cpRedCharacterCriticalInjuriesRequest.getStatus()
        );
        cpRedCharacterCriticalInjuriesRepository.save(newInjury);
        return CustomReturnables.getOkResponseMap("Rany krytyczne zostały dodane do postaci.");
    }

    public Map<String, Object> deleteInjury(Long characterInjuryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterCriticalInjuries characterCriticalInjury = cpRedCharacterCriticalInjuriesRepository.findById(characterInjuryId)
                .orElseThrow(() -> new ResourceNotFoundException("Rana krytyczna postaci o id " + characterInjuryId + " nie została znaleziona"));

        CpRedCharacters character = cpRedCharactersRepository.findById(characterCriticalInjury.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + characterCriticalInjury.getCharacterId().getId() + " nie została znaleziona"));

        CpRedCriticalInjuries injury = cpRedCriticalInjuriesRepository.findById(characterCriticalInjury.getInjuriesId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Rana krytyczna o id " + characterCriticalInjury.getInjuriesId().getId() + " nie została znaleziona"));

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

        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może usunąć pancerz postaci NPC.");
            }
        }
        cpRedCharacterCriticalInjuriesRepository.delete(characterCriticalInjury);
        return CustomReturnables.getOkResponseMap("Rana krytyczna postaci o id " + characterInjuryId + " została usunięta.");
    }

    public Map<String, Object> updateInjury(Long characterInjuryId, CpRedCharacterCriticalInjuriesRequest cpRedCharacterCriticalInjuries) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterCriticalInjuries injuryToUpdate = cpRedCharacterCriticalInjuriesRepository.findById(characterInjuryId)
                .orElseThrow(() -> new ResourceNotFoundException("Rana krytyczna o id " + characterInjuryId + " nie została znaleziona"));

        CpRedCharacters character = cpRedCharactersRepository.findById(injuryToUpdate.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o id " + injuryToUpdate.getCharacterId().getId() + " nie została znaleziona"));

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

        if(character.getUser()==null){
            if( gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
                throw new IllegalStateException("Nie masz uprawnień do modyfikowania ran krytycznych dla tej postaci.");
            }
        } else if (gameUsers.getRole()!= GameUsersRole.GAMEMASTER){
            if (!character.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Nie masz uprawnień do modyfikowania ran krytycznych dla tej postaci.");
            }
        }

        if (injuryToUpdate.getCharacterId().getId() != cpRedCharacterCriticalInjuries.getCharacterId()) {
            throw new IllegalArgumentException("Nie można zmienić postaci tej rany krytycznej.");
        }

        if(cpRedCharacterCriticalInjuries.getInjuriesId() != null ) {
            CpRedCriticalInjuries injuries = cpRedCriticalInjuriesRepository.findById(cpRedCharacterCriticalInjuries.getInjuriesId())
                    .orElseThrow(() -> new ResourceNotFoundException("Rana krytyczna o id " + cpRedCharacterCriticalInjuries.getInjuriesId() + " nie została znaleziona"));
            if(injuries!=null){
                injuryToUpdate.setInjuriesId(injuries);
            }
        }
        if (cpRedCharacterCriticalInjuries.getStatus() != null) {
            injuryToUpdate.setStatus(cpRedCharacterCriticalInjuries.getStatus());
        }

        cpRedCharacterCriticalInjuriesRepository.save(injuryToUpdate);
        return CustomReturnables.getOkResponseMap("Rana krytyczna została zaktualizowana.");
    }


}

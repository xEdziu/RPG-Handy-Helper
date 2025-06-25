package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries.CpRedCharacterCriticalInjuriesStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries.CpRedCharacterCustomCriticalInjuries;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries.CpRedCharacterCustomCriticalInjuries;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries.UpdateCharacterCustomCriticalInjuriesRequest;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries.AddCharacterCustomCriticalInjuriesRequest;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries.CpRedCharacterCustomCriticalInjuries;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries.CpRedCharacterCustomCriticalInjuriesDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries.CpRedCharacterCustomCriticalInjuriesRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCriticalInjuries.CpRedCustomCriticalInjuries;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCriticalInjuries.CpRedCustomCriticalInjuriesRepository;
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
public class CpRedCharacterCustomCriticalInjuriesService {
    private final CpRedCharacterCustomCriticalInjuriesRepository characterCustomCriticalInjuriesRepository;
    private final CpRedCustomCriticalInjuriesRepository customCriticalInjuriesRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;

    public Map<String, Object> getCharacterCustomCriticalInjuries(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterCustomCriticalInjuriesDTO> characterCustomCriticalInjuriesList = characterCustomCriticalInjuriesRepository
                .findAllByCharacterId(characterId)
                .stream()
                .map(characterCustomCriticalInjuries -> new CpRedCharacterCustomCriticalInjuriesDTO(
                        characterCustomCriticalInjuries.getId(),
                        characterCustomCriticalInjuries.getCharacter().getId(),
                        characterCustomCriticalInjuries.getCustomInjuries().getId(),
                        characterCustomCriticalInjuries.getStatus().toString()
                ))
                .toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe rany krytyczne postaci pobrane pomyślnie");
        response.put("characterCustomCriticalInjuries", characterCustomCriticalInjuriesList);
        return response;
    }

    public Map<String, Object> createCharacterCustomCriticalInjuries(AddCharacterCustomCriticalInjuriesRequest addCharacterCustomCriticalInjuriesRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        if(addCharacterCustomCriticalInjuriesRequest.getCharacterId() == null ||
                addCharacterCustomCriticalInjuriesRequest.getCustomInjuriesId() == null ||
                addCharacterCustomCriticalInjuriesRequest.getStatus() == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterCustomCriticalInjuriesRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        CpRedCustomCriticalInjuries customCriticalInjuries = customCriticalInjuriesRepository.findById(addCharacterCustomCriticalInjuriesRequest.getCustomInjuriesId())
                .orElseThrow(() -> new ResourceNotFoundException("Customowe rany krytyczne o podanym ID nie zostały znalezione."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        if (!Objects.equals(character.getGame().getId(), customCriticalInjuries.getGameId().getId())) {
            throw new ResourceNotFoundException("Postać i customowe rany krytyczne nie należą do tej samej gry.");
        }

        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może dodawać customowe rany krytyczne postaci NPC.");
            }
        }

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        if (characterCustomCriticalInjuriesRepository.existsByCharacterAndCustomInjuries(character, customCriticalInjuries)) {
            throw new IllegalArgumentException("Postać ma już przypisane te customowe rany krytyczne.");
        }

        CpRedCharacterCustomCriticalInjuries newCharacterCustomCriticalInjuries = new CpRedCharacterCustomCriticalInjuries(
                null,
                character,
                customCriticalInjuries,
                addCharacterCustomCriticalInjuriesRequest.getStatus()
        );

        characterCustomCriticalInjuriesRepository.save(newCharacterCustomCriticalInjuries);

        return CustomReturnables.getOkResponseMap("Customowe rany krytyczne zostały dodane do postaci");
    }

    public Map<String, Object> updateCharacterCustomCriticalInjuries(Long characterCustomCriticalInjuriesId,
                                                               UpdateCharacterCustomCriticalInjuriesRequest updateCharacterCustomCriticalInjuriesRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        
        CpRedCharacterCustomCriticalInjuries characterCustomCriticalInjuriesToUpdate = characterCustomCriticalInjuriesRepository.findById(characterCustomCriticalInjuriesId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowe rany krytyczne postaci o podanym ID nie zostały znalezione."));
        
        CpRedCharacters character = cpRedCharactersRepository.findById(characterCustomCriticalInjuriesToUpdate.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));
        
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));
        
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może zmieniać customowe rany krytyczne postaci NPC.");
            }
        }
        
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        if (updateCharacterCustomCriticalInjuriesRequest.getStatus() != null) {
            characterCustomCriticalInjuriesToUpdate.setStatus(updateCharacterCustomCriticalInjuriesRequest.getStatus());
        }
        
        characterCustomCriticalInjuriesRepository.save(characterCustomCriticalInjuriesToUpdate);

        return CustomReturnables.getOkResponseMap("Customowe rany krytyczne postaci zostały pomyślnie zmodyfikowane");
    }

    public Map<String, Object> deleteCharacterCustomCriticalInjuries(Long characterCustomCriticalInjuriesId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        
        CpRedCharacterCustomCriticalInjuries characterCustomCriticalInjuriesToDelete = characterCustomCriticalInjuriesRepository.findById(characterCustomCriticalInjuriesId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowe rany krytyczne postaci o podanym ID nie zostały znalezione."));
        
        CpRedCharacters character = cpRedCharactersRepository.findById(characterCustomCriticalInjuriesToDelete.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));
        
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));
        
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może zmieniać customowe rany krytyczne postaci NPC.");
            }
        }

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        characterCustomCriticalInjuriesRepository.deleteById(characterCustomCriticalInjuriesId);

        return CustomReturnables.getOkResponseMap("Customowe rany krytyczne postaci zostały pomyślnie usunięte");
    }

    public Map<String, Object> getAllCharacterCustomCriticalInjuries() {
        List<CpRedCharacterCustomCriticalInjuries> allCharacterCustomCriticalInjuries = characterCustomCriticalInjuriesRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie customowe rany krytyczne postaci pobrane pomyślnie");
        response.put("allCharacterCustomCriticalInjuries", allCharacterCustomCriticalInjuries);
        return response;
    }

    
    
}

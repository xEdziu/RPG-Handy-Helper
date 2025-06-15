package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomAmmunition;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition.CpRedCharacterAmmunition;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunition;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunitionRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition.CpRedAmmunition;
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
public class CpRedCharacterCustomAmmunitionService {
    private final CpRedCharacterCustomAmmunitionRepository characterCustomAmmunitionRepository;
    private final CpRedCustomAmmunitionRepository customAmmunitionRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;

    public Map<String, Object> getCharacterCustomAmmunition(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterCustomAmmunitionDTO> characterCustomAmmunitionList = characterCustomAmmunitionRepository
                .findAllByCharacterId(characterId)
                .stream()
                .map(characterCustomAmmunition -> new CpRedCharacterCustomAmmunitionDTO(
                        characterCustomAmmunition.getId(),
                        characterCustomAmmunition.getCharacter().getId(),
                        characterCustomAmmunition.getCustomAmmunition().getId(),
                        characterCustomAmmunition.getStatus().toString(),
                        characterCustomAmmunition.getAmount()
                        ))
                .toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe amunicje postaci pobrane pomyślnie");
        response.put("characterCustomAmmunition", characterCustomAmmunitionList);
        return response;
    }

    public Map<String, Object> createCharacterCustomAmmunition(AddCharacterCustomAmmunitionRequest addCharacterCustomAmmunitionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        if(addCharacterCustomAmmunitionRequest.getCharacterId() == null ||
                addCharacterCustomAmmunitionRequest.getCustomAmmunitionId() == null ||
                addCharacterCustomAmmunitionRequest.getStatus() == null ||
                addCharacterCustomAmmunitionRequest.getAmount() == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterCustomAmmunitionRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje broń o podanym ID
        CpRedCustomAmmunition customAmmunition = customAmmunitionRepository.findById(addCharacterCustomAmmunitionRequest.getCustomAmmunitionId())
                .orElseThrow(() -> new ResourceNotFoundException("Customowa amunicja o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        // Czy użytkownik należy do gry, do której należy postać
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        // Czy ktoś chce zmieniać swoją postać lub jest GM-em w tej grze
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może dodawać customowe amunicje postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Czy istnieje już taka amunicja dla tej postaci
        if (characterCustomAmmunitionRepository.existsByCharacterAndCustomAmmunition(character, customAmmunition)) {
            throw new IllegalArgumentException("Postać ma już przypisaną tę customową amunicję.");
        }

        if (addCharacterCustomAmmunitionRequest.getAmount() < 0) {
            throw new IllegalArgumentException("Ilość customowej amunicji nie może być mniejsza niż 0.");
        }

        CpRedCharacterCustomAmmunition newCharacterCustomAmmunition = new CpRedCharacterCustomAmmunition(
                null,
                character,
                customAmmunition,
                addCharacterCustomAmmunitionRequest.getStatus(),
                addCharacterCustomAmmunitionRequest.getAmount()
        );

        characterCustomAmmunitionRepository.save(newCharacterCustomAmmunition);

        return CustomReturnables.getOkResponseMap("Customowa amunicja została dodana do postaci");
    }

    public Map<String, Object> updateCharacterCustomAmmunition(Long characterCustomAmmunitionId,
                                                               UpdateCharacterCustomAmmunitionRequest updateCharacterCustomAmmunitionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje amunicja postaci o podanym ID
        CpRedCharacterCustomAmmunition characterCustomAmmunitionToUpdate = characterCustomAmmunitionRepository.findById(characterCustomAmmunitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowa amunicja postaci o podanym ID nie została znaleziona."));

        // Czy istnieje postać o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterCustomAmmunitionToUpdate.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        // Czy użytkownik należy do gry, do której należy postać
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        // Czy ktoś chce zmieniać swoją postać lub jest GM-em w tej grze
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może zmieniać customowe amunicje postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        if (updateCharacterCustomAmmunitionRequest.getStatus() != null) {
            characterCustomAmmunitionToUpdate.setStatus(updateCharacterCustomAmmunitionRequest.getStatus());
        }

        if (updateCharacterCustomAmmunitionRequest.getAmount() != null){
            if (updateCharacterCustomAmmunitionRequest.getAmount() < 0) {
                throw new IllegalArgumentException("Ilość customowej amunicji nie może być mniejsza niż 0.");
            }
            characterCustomAmmunitionToUpdate.setAmount(updateCharacterCustomAmmunitionRequest.getAmount());
        }

        characterCustomAmmunitionRepository.save(characterCustomAmmunitionToUpdate);

        return CustomReturnables.getOkResponseMap("Customowa amunicja postaci została pomyślnie zmodyfikowana");
    }

    public Map<String, Object> deleteCharacterCustomAmmunition(Long characterCustomAmmunitionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje amunicja postaci o podanym ID
        CpRedCharacterCustomAmmunition characterCustomAmmunitionToDelete = characterCustomAmmunitionRepository.findById(characterCustomAmmunitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowa amunicja postaci o podanym ID nie została znaleziona."));

        // Czy istnieje postać o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterCustomAmmunitionToDelete.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        // Czy użytkownik należy do gry, do której należy postać
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        // Czy ktoś chce zmieniać swoją postać lub jest GM-em w tej grze
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może zmieniać customowe amunicje postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Usunięcie amunicji postaci
        characterCustomAmmunitionRepository.deleteById(characterCustomAmmunitionId);

        return CustomReturnables.getOkResponseMap("Customowa amunicja postaci została pomyślnie usunięta");
    }

    // ============ Admin methods ============
    public Map<String, Object> getAllCharacterCustomAmmunition() {
        List<CpRedCharacterCustomAmmunition> allCharacterCustomAmmunition = characterCustomAmmunitionRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie customowe amunicje postaci pobrane pomyślnie");
        response.put("allCharacterCustomAmmunition", allCharacterCustomAmmunition);
        return response;
    }
}

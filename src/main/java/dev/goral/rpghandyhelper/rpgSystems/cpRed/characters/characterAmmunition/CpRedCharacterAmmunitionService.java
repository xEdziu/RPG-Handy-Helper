package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon.CpRedCharacterCustomWeaponRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeaponRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.ammunition.CpRedAmmunitionCompatibilityRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunitionRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition.CpRedAmmunition;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition.CpRedAmmunitionRepository;
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
public class CpRedCharacterAmmunitionService {
    private final CpRedCharacterAmmunitionRepository characterAmmunitionRepository;
    private final CpRedAmmunitionRepository ammunitionRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;

    public Map<String, Object> getCharacterAmmunition(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterAmmunitionDTO> characterAmunutionList = characterAmmunitionRepository.findAllByCharacterId(characterId)
                .stream()
                .map(characterAmmunition -> new CpRedCharacterAmmunitionDTO(
                        characterAmmunition.getId(),
                        characterAmmunition.getCharacter().getId(),
                        characterAmmunition.getAmmunition().getId(),
                        characterAmmunition.getStatus().toString(),
                        characterAmmunition.getAmount()
                ))
                .toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Amunicje postaci pobrane pomyślnie");
        response.put("characterAmmunition", characterAmunutionList);
        return response;
    }

    public Map<String, Object> createCharacterAmmunition(AddCharacterAmmunitionRequest addCharacterAmmunitionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        if(addCharacterAmmunitionRequest.getCharacterId() == null ||
                addCharacterAmmunitionRequest.getAmmunitionId() == null ||
                addCharacterAmmunitionRequest.getStatus() == null ||
                addCharacterAmmunitionRequest.getAmount() == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterAmmunitionRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje broń o podanym ID
        CpRedAmmunition ammunition = ammunitionRepository.findById(addCharacterAmmunitionRequest.getAmmunitionId())
                .orElseThrow(() -> new ResourceNotFoundException("Amunicja o podanym ID nie została znaleziona."));

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
                throw new ResourceNotFoundException("Tylko GM może dodawać amunicje postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Czy istnieje już taka amunicja dla tej postaci
        if (characterAmmunitionRepository.existsByCharacterAndAmmunition(character, ammunition)) {
            throw new IllegalArgumentException("Postać ma już przypisaną tę amunicję.");
        }

        if (addCharacterAmmunitionRequest.getAmount() < 0) {
            throw new IllegalArgumentException("Ilość amunicji nie może być mniejsza niż 0.");
        }

        CpRedCharacterAmmunition newCharacterAmmunition = new CpRedCharacterAmmunition(
                null,
                character,
                ammunition,
                addCharacterAmmunitionRequest.getStatus(),
                addCharacterAmmunitionRequest.getAmount()
        );

        characterAmmunitionRepository.save(newCharacterAmmunition);

        return CustomReturnables.getOkResponseMap("Amunicja została dodana do postaci");
    }

    public Map<String, Object> updateCharacterAmmunition(Long characterAmmunitionId,
                                                         UpdateCharacterAmmunitionRequest updateCharacterAmmunitionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje amunicja postaci o podanym ID
        CpRedCharacterAmmunition characterAmmunitionToUpdate = characterAmmunitionRepository.findById(characterAmmunitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Amunicja postaci o podanym ID nie została znaleziona."));

        // Czy istnieje postać o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterAmmunitionToUpdate.getCharacter().getId())
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
                throw new ResourceNotFoundException("Tylko GM może zmieniać amunicje postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        if (updateCharacterAmmunitionRequest.getStatus() != null) {
            characterAmmunitionToUpdate.setStatus(updateCharacterAmmunitionRequest.getStatus());
        }

        if (updateCharacterAmmunitionRequest.getAmount() != null){
            if (updateCharacterAmmunitionRequest.getAmount() < 0) {
                throw new IllegalArgumentException("Ilość amunicji nie może być mniejsza niż 0.");
            }
            characterAmmunitionToUpdate.setAmount(updateCharacterAmmunitionRequest.getAmount());
        }

        characterAmmunitionRepository.save(characterAmmunitionToUpdate);

        return CustomReturnables.getOkResponseMap("Amunicja postaci została pomyślnie zmodyfikowana");
    }

    public Map<String, Object> deleteCharacterAmmunition(Long characterAmmunitionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje amunicja postaci o podanym ID
        CpRedCharacterAmmunition characterAmmunitionToDelete = characterAmmunitionRepository.findById(characterAmmunitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Amunicja postaci o podanym ID nie została znaleziona."));

        // Czy istnieje postać o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterAmmunitionToDelete.getCharacter().getId())
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
                throw new ResourceNotFoundException("Tylko GM może zmieniać amunicje postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Usunięcie amunicji postaci
        characterAmmunitionRepository.deleteById(characterAmmunitionId);

        return CustomReturnables.getOkResponseMap("Amunicja postaci została pomyślnie usunięta");
    }

    public Map<String, Object> getAllCharacterAmmunition() {
        List<CpRedCharacterAmmunition> allCharacterAmmunition = characterAmmunitionRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie amunicje postaci pobrane pomyślnie");
        response.put("allCharacterAmmunition", allCharacterAmmunition);
        return response;
    }
}

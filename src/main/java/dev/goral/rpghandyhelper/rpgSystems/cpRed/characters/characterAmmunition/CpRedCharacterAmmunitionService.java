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
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon.CpRedCharacterCustomWeapon;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon.CpRedCharacterCustomWeaponRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeapon;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeaponRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.ammunition.CpRedAmmunitionCompatibilityRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunition;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunitionRepository;
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
    private final CpRedCustomAmmunitionRepository customAmmunitionRepository;
    private final CpRedAmmunitionCompatibilityRepository ammunitionCompatibilityRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedCharacterWeaponRepository characterWeaponRepository;
    private final CpRedCharacterCustomWeaponRepository characterCustomWeaponRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;

    public Map<String, Object> getCharacterAmmunition(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterAmmunitionDTO> characterAmunutionList = characterAmmunitionRepository.findAllByCharacterId(characterId)
                .stream()
                .map(characterAmmunition -> new CpRedCharacterAmmunitionDTO(
                        characterAmmunition.getId(),
                        characterAmmunition.getCharacterId(),
                        characterAmmunition.getCharacterWeaponId(),
                        characterAmmunition.getIsCharacterWeaponCustom(),
                        characterAmmunition.getAmmunitionId(),
                        characterAmmunition.getIsAmmunitionCustom(),
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

        // Czy podano wszystkie wymagane pola w request
        if (addCharacterAmmunitionRequest.getCharacterId() == null ||
                addCharacterAmmunitionRequest.getCharacterWeaponId() == null ||
                addCharacterAmmunitionRequest.getIsCharacterWeaponCustom() == null ||
                addCharacterAmmunitionRequest.getAmmunitionId() == null ||
                addCharacterAmmunitionRequest.getIsAmmunitionCustom() == null ||
                addCharacterAmmunitionRequest.getAmount() == null) {
            throw new IllegalArgumentException("Wszystkie pola są wymagane.");
        }

        // Czy jest już kombinacja tej broni i tej amunicji u tej postaci
        if (characterAmmunitionRepository.existsByCharacterIdAndCharacterWeaponIdAndIsCharacterWeaponCustomAndAmmunitionIdAndIsAmmunitionCustom(
                addCharacterAmmunitionRequest.getCharacterId(),
                addCharacterAmmunitionRequest.getCharacterWeaponId(),
                addCharacterAmmunitionRequest.getIsCharacterWeaponCustom(),
                addCharacterAmmunitionRequest.getAmmunitionId(),
                addCharacterAmmunitionRequest.getIsAmmunitionCustom())) {
            throw new IllegalArgumentException("Ta kombinacja broni i amunicji już istnieje u tej postaci.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterAmmunitionRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        boolean isCharacterWeaponCustom = addCharacterAmmunitionRequest.getIsCharacterWeaponCustom();
        boolean isAmmunitionCustom = addCharacterAmmunitionRequest.getIsAmmunitionCustom();

        if (isCharacterWeaponCustom){
            // Czy istnieje customowa broń o podanym ID
            CpRedCharacterCustomWeapon characterCustomWeapon = characterCustomWeaponRepository.findById(addCharacterAmmunitionRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customowa broń o podanym ID nie została znaleziona u tej postaci."));
            // Czy postać należy do tej samej gry co customowa broń
            if (!Objects.equals(characterCustomWeapon.getCharacter().getGame().getId(), character.getGame().getId())) {
                throw new ResourceNotFoundException("Customowa broń o podanym ID nie należy do tej samej gry co postaci.");
            }
            // Czy podana postać i postać posiadająca customową broń się zgadzają
            if (!Objects.equals(characterCustomWeapon.getCharacter().getId(), character.getId())) {
                throw new ResourceNotFoundException("Customowa broń o podanym ID nie należy do tej postaci.");
            }

        } else {
            // Czy istnieje broń o podanym ID
            CpRedCharacterWeapon characterWeapon = characterWeaponRepository.findById(addCharacterAmmunitionRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Broń o podanym ID nie została znaleziona u tej postaci."));
            // Czy podana postać i postać posiadająca broń się zgadzają
            if (!Objects.equals(characterWeapon.getCharacter().getId(), character.getId())) {
                throw new ResourceNotFoundException("Broń o podanym ID nie należy do tej postaci.");
            }
        }

        if (isAmmunitionCustom) {
            // Czy istnieje customowa amunicja o podanym ID
            CpRedCustomAmmunition customAmmunition = customAmmunitionRepository.findById(addCharacterAmmunitionRequest.getAmmunitionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customowa amunicja o podanym ID nie została znaleziona u tej postaci."));
            // Czy postać należy do tej samej gry co customowa amunicja
            if (!Objects.equals(customAmmunition.getGameId().getId(), character.getGame().getId())) {
                throw new ResourceNotFoundException("Customowa amunicja o podanym ID nie należy do tej samej gry co postaci.");
            }
        } else {
            // Czy istnieje amunicja o podanym ID
            ammunitionRepository.findById(addCharacterAmmunitionRequest.getAmmunitionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Amunicja o podanym ID nie została znaleziona u tej postaci."));
        }

        CpRedCharacterWeapon characterWeapon = null;
        CpRedCharacterCustomWeapon characterCustomWeapon = null;
        // Sprawdzenie kompatybilności broni i amunicji
        if (isAmmunitionCustom &&
                isCharacterWeaponCustom) {
            characterCustomWeapon = characterCustomWeaponRepository.findById(addCharacterAmmunitionRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customowa broń o podanym ID nie została znaleziona u tej postaci."));
            // Czy istnieje kompatybilność customowej amunicji i customowej broni
            if (!ammunitionCompatibilityRepository.existsByWeaponIdAndAmmunitionIdAndIsWeaponCustomAndIsAmmunitionCustom(
                    characterCustomWeapon.getBaseCustomWeapon().getId(),
                    addCharacterAmmunitionRequest.getAmmunitionId(),
                    true,
                    true)) {
                throw new ResourceNotFoundException("Customowa amunicja nie jest kompatybilna z tą customową bronią.");
            }
        } else if (isAmmunitionCustom &&
                !isCharacterWeaponCustom) {
            characterWeapon = characterWeaponRepository.findById(addCharacterAmmunitionRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Broń o podanym ID nie została znaleziona u tej postaci."));
            // Czy istnieje kompatybilność customowej amunicji i broni
            if (!ammunitionCompatibilityRepository.existsByWeaponIdAndAmmunitionIdAndIsWeaponCustomAndIsAmmunitionCustom(
                    characterWeapon.getBaseWeapon().getId(),
                    addCharacterAmmunitionRequest.getAmmunitionId(),
                    false,
                    true)) {
                throw new ResourceNotFoundException("Customowa amunicja nie jest kompatybilna z tą bronią.");
            }
        } else if (!isAmmunitionCustom &&
                isCharacterWeaponCustom) {
            characterCustomWeapon = characterCustomWeaponRepository.findById(addCharacterAmmunitionRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customowa broń o podanym ID nie została znaleziona u tej postaci."));
            // Czy istnieje kompatybilność amunicji i customowej broni
            if (!ammunitionCompatibilityRepository.existsByWeaponIdAndAmmunitionIdAndIsWeaponCustomAndIsAmmunitionCustom(
                    characterCustomWeapon.getBaseCustomWeapon().getId(),
                    addCharacterAmmunitionRequest.getAmmunitionId(),
                    true,
                    false)) {
                throw new ResourceNotFoundException("Amunicja nie jest kompatybilna z tą customową bronią.");
            }
        } else if (!isAmmunitionCustom &&
                !isCharacterWeaponCustom) {
            characterWeapon = characterWeaponRepository.findById(addCharacterAmmunitionRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Broń o podanym ID nie została znaleziona u tej postaci."));
            // Czy istnieje kompatybilność amunicji i broni
            if (!ammunitionCompatibilityRepository.existsByWeaponIdAndAmmunitionIdAndIsWeaponCustomAndIsAmmunitionCustom(
                    characterWeapon.getBaseWeapon().getId(),
                    addCharacterAmmunitionRequest.getAmmunitionId(),
                    false,
                    false)) {
                throw new ResourceNotFoundException("Amunicja nie jest kompatybilna z tą bronią.");
            }
        }

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
                throw new ResourceNotFoundException("Tylko GM może dodawać customowe bronie postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Czy ilość amunicji jest większa/równa 0
        if (addCharacterAmmunitionRequest.getAmount() < 0) {
            throw new IllegalArgumentException("Ilość amunicji musi być większa lub równa 0.");
        }

        // Tworzenie nowej amunicji postaci
        CpRedCharacterAmmunition newCharacterAmmunition = new CpRedCharacterAmmunition(
                null,
                addCharacterAmmunitionRequest.getCharacterId(),
                addCharacterAmmunitionRequest.getCharacterWeaponId(),
                addCharacterAmmunitionRequest.getIsCharacterWeaponCustom(),
                addCharacterAmmunitionRequest.getAmmunitionId(),
                addCharacterAmmunitionRequest.getIsAmmunitionCustom(),
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

        // Czy podana amunicja postaci istnieje
        CpRedCharacterAmmunition characterAmmunitionToUpdate = characterAmmunitionRepository.findById(characterAmmunitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Amunicja postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterAmmunitionToUpdate.getCharacterId())
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
                throw new ResourceNotFoundException("Tylko GM może dodawać customowe bronie postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Sprawdzenie podanej ilości amunicji
        if (updateCharacterAmmunitionRequest.getAmount() != null) {
            if (updateCharacterAmmunitionRequest.getAmount() < 0) {
                throw new IllegalArgumentException("Ilość amunicji musi być większa lub równa 0.");
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

        // Czy podana amunicja postaci istnieje
        CpRedCharacterAmmunition characterAmmunitionToDelete = characterAmmunitionRepository.findById(characterAmmunitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Amunicja postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterAmmunitionToDelete.getCharacterId())
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
                throw new ResourceNotFoundException("Tylko GM może dodawać customowe bronie postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Usuwanie amunicji postaci
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

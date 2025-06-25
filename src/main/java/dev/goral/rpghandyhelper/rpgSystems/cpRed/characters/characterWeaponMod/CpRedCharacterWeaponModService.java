package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeaponMod;

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
import dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.weaponMod.CpRedModCompatibilityRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeaponMods.CpRedCustomWeaponMods;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeaponMods.CpRedCustomWeaponModsRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weaponMods.CpRedWeaponMods;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weaponMods.CpRedWeaponModsRepository;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CpRedCharacterWeaponModService {
    private final CpRedCharacterWeaponModRepository cpRedCharacterWeaponModRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedCharacterWeaponRepository characterWeaponRepository;
    private final CpRedCharacterCustomWeaponRepository characterCustomWeaponRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final CpRedWeaponModsRepository cpRedWeaponModsRepository;
    private final CpRedCustomWeaponModsRepository cpRedCustomWeaponModsRepository;
    private final CpRedModCompatibilityRepository cpRedModCompatibilityRepository;


    public Map<String, Object> getCharacterWeaponMods(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterWeaponModDTO> characterWeaponMods = cpRedCharacterWeaponModRepository.findAllByCharacterId(characterId)
                .stream()
                .map(characterWeaponMod -> new CpRedCharacterWeaponModDTO(
                        characterWeaponMod.getId(),
                        characterWeaponMod.getCharacterId(),
                        characterWeaponMod.getCharacterWeaponId(),
                        characterWeaponMod.getIsCharacterWeaponCustom(),
                        characterWeaponMod.getWeaponModId(),
                        characterWeaponMod.getIsWeaponModCustom(),
                        characterWeaponMod.getSizeTaken()))
                .toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Modyfikacje broni postaci pobrane pomyślnie");
        response.put("characterWeaponMod", characterWeaponMods);
        return response;
    }

    public Map<String, Object> getCharacterWeaponModsByCharacterWeaponId(GetModsForWeaponRequest getModsForWeaponRequest) {
        if (getModsForWeaponRequest.getIsCharacterWeaponCustom() == null || getModsForWeaponRequest.getCharacterWeaponId() == null) {
            throw new IllegalArgumentException("Nie podano wymaganych parametrów.");
        }
        if(getModsForWeaponRequest.getIsCharacterWeaponCustom()){
            CpRedCharacterCustomWeapon characterCustomWeapon = characterCustomWeaponRepository.findById(getModsForWeaponRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Niestandardowa broń postaci o podanym ID nie została znaleziona."));
        } else {
            CpRedCharacterWeapon characterWeapon = characterWeaponRepository.findById(getModsForWeaponRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Standardowa broń postaci o podanym ID nie została znaleziona."));
        }

        List<CpRedCharacterWeaponModDTO> characterWeaponMods =
                cpRedCharacterWeaponModRepository.findAllByCharacterWeaponIdAndIsCharacterWeaponCustom(
                        getModsForWeaponRequest.getCharacterWeaponId(),
                        getModsForWeaponRequest.getIsCharacterWeaponCustom()).
                        stream().map(characterWeaponMod -> new CpRedCharacterWeaponModDTO(
                                characterWeaponMod.getId(),
                                characterWeaponMod.getCharacterId(),
                                characterWeaponMod.getCharacterWeaponId(),
                                characterWeaponMod.getIsCharacterWeaponCustom(),
                                characterWeaponMod.getWeaponModId(),
                                characterWeaponMod.getIsWeaponModCustom(),
                                characterWeaponMod.getSizeTaken()))
                        .toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Modyfikacje konkretnej broni postaci pobrane pomyślnie");
        response.put("characterWeaponMod", characterWeaponMods);
        return response;
    }

    public Map<String, Object> createCharacterWeaponMod(AddCharacterWeaponModRequest addCharacterWeaponModRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy podano wszystkie wymagane pola w request
        if( addCharacterWeaponModRequest.getCharacterId() == null ||
                addCharacterWeaponModRequest.getCharacterWeaponId() == null ||
                addCharacterWeaponModRequest.getIsCharacterWeaponCustom() == null ||
                addCharacterWeaponModRequest.getWeaponModId() == null ||
                addCharacterWeaponModRequest.getIsWeaponModCustom() == null) {
            throw new IllegalArgumentException("Nie podano wymaganych pól w request.");
        }

        // Czy jest już kombinacja tej broni i tej modyfikacji u tej postaci
        if( cpRedCharacterWeaponModRepository.existsByCharacterIdAndCharacterWeaponIdAndIsCharacterWeaponCustomAndWeaponModIdAndIsWeaponModCustom(
                addCharacterWeaponModRequest.getCharacterId(),
                addCharacterWeaponModRequest.getCharacterWeaponId(),
                addCharacterWeaponModRequest.getIsCharacterWeaponCustom(),
                addCharacterWeaponModRequest.getWeaponModId(),
                addCharacterWeaponModRequest.getIsWeaponModCustom())) {
            throw new IllegalArgumentException("Ta modyfikacja broni jest już przypisana do tej broni postaci.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterWeaponModRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        boolean isCharacterWeaponCustom = addCharacterWeaponModRequest.getIsCharacterWeaponCustom();
        boolean isWeaponModCustom = addCharacterWeaponModRequest.getIsWeaponModCustom();

        if (isCharacterWeaponCustom){
            // Czy istnieje customowa broń o podanym ID
            CpRedCharacterCustomWeapon characterCustomWeapon = characterCustomWeaponRepository.findById(addCharacterWeaponModRequest.getCharacterWeaponId())
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
            CpRedCharacterWeapon characterWeapon = characterWeaponRepository.findById(addCharacterWeaponModRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Broń o podanym ID nie została znaleziona u tej postaci."));
            // Czy podana postać i postać posiadająca broń się zgadzają
            if (!Objects.equals(characterWeapon.getCharacter().getId(), character.getId())) {
                throw new ResourceNotFoundException("Broń o podanym ID nie należy do tej postaci.");
            }
        }

        if (isWeaponModCustom) {
            // Czy istnieje customowa modyfikacja broni o podanym ID
            CpRedCustomWeaponMods customWeaponMod = cpRedCustomWeaponModsRepository.findById(addCharacterWeaponModRequest.getWeaponModId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customowa modyfikacja broni o podanym ID nie została znaleziona."));
            // Czy postać należy do tej samej gry co customowa modyfikacja broni
            if (!Objects.equals(customWeaponMod.getGameId().getId(), game.getId())) {
                throw new ResourceNotFoundException("Customowa modyfikacja broni o podanym ID nie należy do tej samej gry co postaci.");
            }
        } else {
            // Czy istnieje modyfikacja o podanym ID
            CpRedWeaponMods weaponMod = cpRedWeaponModsRepository.findById(addCharacterWeaponModRequest.getWeaponModId())
                    .orElseThrow(() -> new ResourceNotFoundException("Modyfikacja broni o podanym ID nie została znaleziona."));

        }

        CpRedCharacterWeapon characterWeapon = null;
        CpRedCharacterCustomWeapon characterCustomWeapon = null;
        // Sprawdzenie kompatybilności broni i modyfikacji
        if (isWeaponModCustom &&
                isCharacterWeaponCustom) {
            characterCustomWeapon = characterCustomWeaponRepository.findById(addCharacterWeaponModRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customowa broń o podanym ID nie została znaleziona u tej postaci."));
            // Czy istnieje kompatybilność customowej amunicji i customowej broni
            if (!cpRedModCompatibilityRepository.existsByWeaponIdAndModIdAndIsWeaponCustomAndIsModCustom(
                    characterCustomWeapon.getBaseCustomWeapon().getId(),
                    addCharacterWeaponModRequest.getWeaponModId(),
                    true,
                    true)) {
                throw new ResourceNotFoundException("Customowa modyfikacja broni nie jest kompatybilna z tą customową bronią.");
            }
        } else if (isWeaponModCustom &&
                !isCharacterWeaponCustom) {
            characterWeapon = characterWeaponRepository.findById(addCharacterWeaponModRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Broń o podanym ID nie została znaleziona u tej postaci."));
            // Czy istnieje kompatybilność customowej amunicji i broni
            if (!cpRedModCompatibilityRepository.existsByWeaponIdAndModIdAndIsWeaponCustomAndIsModCustom(
                    characterWeapon.getBaseWeapon().getId(),
                    addCharacterWeaponModRequest.getWeaponModId(),
                    false,
                    true)) {
                throw new ResourceNotFoundException("Customowa modyfikacja broni nie jest kompatybilna z tą bronią.");
            }
        } else if (!isWeaponModCustom &&
                isCharacterWeaponCustom) {
            characterCustomWeapon = characterCustomWeaponRepository.findById(addCharacterWeaponModRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customowa broń o podanym ID nie została znaleziona u tej postaci."));
            // Czy istnieje kompatybilność amunicji i customowej broni
            if (!cpRedModCompatibilityRepository.existsByWeaponIdAndModIdAndIsWeaponCustomAndIsModCustom(
                    characterCustomWeapon.getBaseCustomWeapon().getId(),
                    addCharacterWeaponModRequest.getWeaponModId(),
                    true,
                    false)) {
                throw new ResourceNotFoundException("Modyfikacja broni nie jest kompatybilna z tą customową bronią.");
            }
        } else if (!isWeaponModCustom &&
                !isCharacterWeaponCustom) {
            characterWeapon = characterWeaponRepository.findById(addCharacterWeaponModRequest.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Broń o podanym ID nie została znaleziona u tej postaci."));
            // Czy istnieje kompatybilność amunicji i broni
            if (!cpRedModCompatibilityRepository.existsByWeaponIdAndModIdAndIsWeaponCustomAndIsModCustom(
                    characterWeapon.getBaseWeapon().getId(),
                    addCharacterWeaponModRequest.getWeaponModId(),
                    false,
                    false)) {
                throw new ResourceNotFoundException("Modyfikacja broni nie jest kompatybilna z tą bronią.");
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
                throw new ResourceNotFoundException("Tylko GM może dodawać customowe modyfikacje broni do postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Sprawdzenie zajętego miejsca przez modyfikację
        CpRedCustomWeaponMods customWeaponMod;
        CpRedWeaponMods weaponMod;
        int sizeTaken = 0;
        if (isWeaponModCustom) {
            customWeaponMod = cpRedCustomWeaponModsRepository.findById(addCharacterWeaponModRequest.getWeaponModId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customowa modyfikacja broni o podanym ID nie została znaleziona."));
            sizeTaken = customWeaponMod.getSize();
        } else {
            weaponMod = cpRedWeaponModsRepository.findById(addCharacterWeaponModRequest.getWeaponModId())
                    .orElseThrow(() -> new ResourceNotFoundException("Modyfikacja broni o podanym ID nie została znaleziona."));
            sizeTaken = weaponMod.getSize();
        }
        // Sprawdzenie, czy broń ma wystarczająco miejsca na modyfikację
        if (isCharacterWeaponCustom) {
            if (characterCustomWeapon.getFreeModSlots() >= sizeTaken) {
                characterCustomWeapon.setFreeModSlots((short) (characterCustomWeapon.getFreeModSlots() - sizeTaken));
            } else {
                throw new IllegalArgumentException("Customowa broń nie ma wystarczająco miejsca na tę modyfikację.");
            }
        } else {
            if (characterWeapon.getFreeModSlots() >= sizeTaken) {
                characterWeapon.setFreeModSlots((short) (characterWeapon.getFreeModSlots() - sizeTaken));
            } else {
                throw new IllegalArgumentException("Broń nie ma wystarczająco miejsca na tę modyfikację.");
            }
        }

        // Tworzenie nowej modyfikacji broni postaci
        CpRedCharacterWeaponMod newCharacterWeaponMod = new CpRedCharacterWeaponMod(
                null,
                addCharacterWeaponModRequest.getCharacterId(),
                addCharacterWeaponModRequest.getCharacterWeaponId(),
                addCharacterWeaponModRequest.getIsCharacterWeaponCustom(),
                addCharacterWeaponModRequest.getWeaponModId(),
                addCharacterWeaponModRequest.getIsWeaponModCustom(),
                sizeTaken
        );

        // Zapisanie modyfikacji broni postaci do bazy danych
        cpRedCharacterWeaponModRepository.save(newCharacterWeaponMod);

        return CustomReturnables.getOkResponseMap("Modyfikacja broni została dodana do postaci");
    }

    @Transactional
    public Map<String, Object> deleteCharacterWeaponMod(Long characterWeaponModId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy podana modyfikacja broni postaci istnieje
        CpRedCharacterWeaponMod characterWeaponModToDelete = cpRedCharacterWeaponModRepository.findById(characterWeaponModId)
                .orElseThrow(() -> new ResourceNotFoundException("Modyfikacja broni postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterWeaponModToDelete.getCharacterId())
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
                throw new ResourceNotFoundException("Tylko GM może usuwać modyfikacja broni postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Zwolnienie zajętych slotów przez modyfikację
        if (characterWeaponModToDelete.getIsCharacterWeaponCustom()) {
            CpRedCharacterCustomWeapon characterCustomWeapon = characterCustomWeaponRepository.findById(characterWeaponModToDelete.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customowa broń o podanym ID nie została znaleziona u tej postaci."));
            characterCustomWeapon.setFreeModSlots((short) (characterCustomWeapon.getFreeModSlots() + characterWeaponModToDelete.getSizeTaken()));
        } else {
            CpRedCharacterWeapon characterWeapon = characterWeaponRepository.findById(characterWeaponModToDelete.getCharacterWeaponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Broń o podanym ID nie została znaleziona u tej postaci."));
            characterWeapon.setFreeModSlots((short) (characterWeapon.getFreeModSlots() + characterWeaponModToDelete.getSizeTaken()));
        }

        // Usunięcie modyfikacji broni postaci z bazy danych
        cpRedCharacterWeaponModRepository.delete(characterWeaponModToDelete);

        return CustomReturnables.getOkResponseMap("Modyfikacje broni postaci została pomyślnie usunięta");
    }

    public Map<String, Object> getAllCharacterWeaponMods() {
        List<CpRedCharacterWeaponMod> allCharacterWeaponMods = cpRedCharacterWeaponModRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie modyfikacje broni postaci pobrane pomyślnie");
        response.put("allCharacterWeaponMods", allCharacterWeaponMods);
        return response;
    }

    public List<CpRedCharacterWeaponModSheetDTO> getCharacterWeaponModForSheet(Long characterId,
                                                                               Long characterWeaponId,
                                                                               Boolean isCharacterWeaponCustom){
        List<CpRedCharacterWeaponMod> characterWeaponMods = cpRedCharacterWeaponModRepository.findAllByCharacterWeaponIdAndIsCharacterWeaponCustom(
                characterWeaponId,
                isCharacterWeaponCustom);
        List<CpRedCharacterWeaponModSheetDTO> characterWeaponModsDTO = new ArrayList<>();
        for(CpRedCharacterWeaponMod mod : characterWeaponMods){
            String weaponModName = "";
            if(mod.getIsWeaponModCustom()){
                CpRedCustomWeaponMods customMod = cpRedCustomWeaponModsRepository.findById(mod.getWeaponModId())
                        .orElseThrow(() -> new ResourceNotFoundException("Broń nie ma customowych modyfikacji"));
                weaponModName = customMod.getName();
            } else {
                CpRedWeaponMods manualMod = cpRedWeaponModsRepository.findById(mod.getWeaponModId())
                        .orElseThrow(() -> new ResourceNotFoundException("Broń nie ma modyfikacji"));
                weaponModName = manualMod.getName();
            }
            CpRedCharacterWeaponModSheetDTO dto = new CpRedCharacterWeaponModSheetDTO(
                    mod.getId(),
                    mod.getWeaponModId(),
                    mod.getIsWeaponModCustom(),
                    weaponModName
            );
            characterWeaponModsDTO.add(dto);
        }

        return characterWeaponModsDTO;
    }
}

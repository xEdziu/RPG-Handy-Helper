package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomArmor.CpRedCharacterCustomArmorRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeapon;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.armors.CpRedArmors;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.armors.CpRedArmorsRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons.CpRedWeapons;
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
public class CpRedCharacterArmorService {
    private final CpRedCharacterArmorRepository cpRedCharacterArmorRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedArmorsRepository cpRedArmorsRepository;
    private final CpRedCharacterCustomArmorRepository cpRedCharacterCustomArmorRepository;

    public Map<String, Object> getCharacterArmor(Long characterId){
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterArmorDTO> characterArmors = cpRedCharacterArmorRepository.findAllByCharacter(character)
                .stream()
                .map(armor -> new CpRedCharacterArmorDTO(
                        armor.getId(),
                        armor.getBaseArmor().getId(),
                        armor.getCharacter().getId(),
                        armor.getStatus().toString(),
                        armor.getPlace().toString(),
                        armor.getCurrentArmorPoints(),
                        armor.getDescription()))
                .toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pancerze postaci pobrane pomyślnie");
        response.put("characterArmors", characterArmors);
        return response;
    }

    public Map<String, Object> createCharacterArmor(AddCharacterArmorRequest addCharacterArmorRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy podano wszystkie wymagane pola w request
        if (addCharacterArmorRequest.getCharacterId() == null ||
                addCharacterArmorRequest.getBaseArmorId() == null ||
                addCharacterArmorRequest.getStatus() == null ||
                addCharacterArmorRequest.getPlace() == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterArmorRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje pancerz o podanym ID
        CpRedArmors armor = cpRedArmorsRepository.findById(addCharacterArmorRequest.getBaseArmorId())
                .orElseThrow(() -> new ResourceNotFoundException("Pancerz o podanym ID nie został znaleziony."));

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
                throw new ResourceNotFoundException("Tylko GM może dodawać pancerz postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Sprawdzenie, czy postać ma już pancerz w tym miejscu
        if (addCharacterArmorRequest.getStatus() == CpRedCharacterItemStatus.EQUIPPED){
            List<CpRedCharacterArmor> placedArmors = cpRedCharacterArmorRepository.findAllByCharacterAndPlace(character, addCharacterArmorRequest.getPlace());
            // Sprawdzenie miejsca pancerza
            for (CpRedCharacterArmor placedArmor : placedArmors) {
                if (placedArmor.getStatus() == CpRedCharacterItemStatus.EQUIPPED && placedArmor.getPlace() == addCharacterArmorRequest.getPlace()) {
                    throw new IllegalArgumentException("Nie można mieć założonych więcej niż jednego pancerza w tym miejscu.");
                }
            }
        }

        // Tworzenie nowej klasy postaci
        CpRedCharacterArmor newCharacterArmor = new CpRedCharacterArmor(
                null, // ID zostanie wygenerowane automatycznie
                armor, // Pancerz bazowy
                character, // Postać
                addCharacterArmorRequest.getStatus(), // Status pancerza
                addCharacterArmorRequest.getPlace(), // Miejsce pancerza
                armor.getArmorPoints(), // Aktualne punkty pancerza
                armor.getDescription() // Opis pancerza
        );

        cpRedCharacterArmorRepository.save(newCharacterArmor);

        return CustomReturnables.getOkResponseMap("Pancerz został dodany do postaci");
    }

    public Map<String, Object> updateCharacterArmor(Long characterArmorId,
                                                    UpdateCharacterArmorRequest updateCharacterArmorRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje pancerz o podanym ID
        CpRedCharacterArmor characterArmorToUpdate = cpRedCharacterArmorRepository.findById(characterArmorId)
                .orElseThrow(() -> new ResourceNotFoundException("Pancerz postaci o podanym ID nie został znaleziony."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterArmorToUpdate.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje broń o podanym ID
        CpRedArmors armor = cpRedArmorsRepository.findById(characterArmorToUpdate.getBaseArmor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pancerz o podanym ID nie został znaleziony."));

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
                throw new ResourceNotFoundException("Tylko GM może modyfikować pancerz postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Sprawdzenie punktów pancerza
        if (updateCharacterArmorRequest.getCurrentArmorPoints() != null){
            if (updateCharacterArmorRequest.getCurrentArmorPoints() < 0) {
                throw new IllegalArgumentException("Aktualne punkty pancerza nie mogą być mniejsze niż 0.");
            }
            if (updateCharacterArmorRequest.getCurrentArmorPoints() > armor.getArmorPoints()) {
                throw new IllegalArgumentException("Aktualne punkty pancerza nie mogą być większe niż maksymalne punkty pancerza.");
            }
            characterArmorToUpdate.setCurrentArmorPoints(updateCharacterArmorRequest.getCurrentArmorPoints());
        }

        // Sprawdzenie statusu pancerza
        if (updateCharacterArmorRequest.getStatus() != null){
            // Czy postać ma już pancerz w tym miejscu
            if (updateCharacterArmorRequest.getStatus() == CpRedCharacterItemStatus.EQUIPPED){
                List<CpRedCharacterArmor> placedArmors = cpRedCharacterArmorRepository.findAllByCharacterAndPlace(character, characterArmorToUpdate.getPlace());
                // Sprawdzenie miejsca pancerza
                for (CpRedCharacterArmor placedArmor : placedArmors) {
                    if (placedArmor.getStatus() == CpRedCharacterItemStatus.EQUIPPED && placedArmor.getPlace() == characterArmorToUpdate.getPlace()) {
                        throw new IllegalArgumentException("Nie można mieć założonych więcej niż jednego pancerza w tym miejscu.");
                    }
                }
            }
            characterArmorToUpdate.setStatus(updateCharacterArmorRequest.getStatus());
        }

        // Sprawdzenie opisu pancerza
        if (updateCharacterArmorRequest.getDescription() != null){
            if (updateCharacterArmorRequest.getDescription().length() > 500){
                throw new IllegalArgumentException("Opis pancerza nie może być dłuższy niż 500 znaków.");
            }
            characterArmorToUpdate.setDescription(updateCharacterArmorRequest.getDescription());
        }

        // Zapisanie zmodyfikowanego pancerza postaci
        cpRedCharacterArmorRepository.save(characterArmorToUpdate);

        return CustomReturnables.getOkResponseMap("Pancerz postaci został pomyślnie zmodyfikowany");
    }

    public Map<String, Object> deleteCharacterArmor(Long characterArmorId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje pancerz o podanym ID
        CpRedCharacterArmor characterArmor = cpRedCharacterArmorRepository.findById(characterArmorId)
                .orElseThrow(() -> new ResourceNotFoundException("Pancerz postaci o podanym ID nie został znaleziony."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterArmor.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        CpRedArmors armor = cpRedArmorsRepository.findById(characterArmor.getBaseArmor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pancerz o podanym ID nie został znaleziony."));

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
                throw new ResourceNotFoundException("Tylko GM może usunąć pancerz postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Usunięcie pancerza postaci
        cpRedCharacterArmorRepository.delete(characterArmor);

        return CustomReturnables.getOkResponseMap("Pancerz postaci został pomyślnie usunięty");
    }

    public Map<String, Object> getAllCharacterArmors(){
        List<CpRedCharacterArmor> allCharacterArmors = cpRedCharacterArmorRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie pancerze postaci pobrane pomyślnie");
        response.put("characterArmors", allCharacterArmors);
        return response;
    }

    public List<CpRedCharacterArmorSheetDTO> getCharacterArmorForSheet(Long characterId, CpRedCharacterItemStatus status){
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterArmor> characterMabualArmorList = cpRedCharacterArmorRepository.findAllByCharacterAndStatus(character, status);
        List<CpRedCharacterArmor> characterMabualArmorList = cpRedCharacterCustomArmorRepository.findAllByCharacterAndStatus(character, status);
    }
}

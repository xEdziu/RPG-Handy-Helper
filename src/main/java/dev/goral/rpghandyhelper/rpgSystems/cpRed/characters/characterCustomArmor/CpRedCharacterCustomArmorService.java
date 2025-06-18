package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomArmor;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customArmors.CpRedCustomArmors;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customArmors.CpRedCustomArmorsRepository;
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
public class CpRedCharacterCustomArmorService {
    private final CpRedCharacterCustomArmorRepository cpRedCharacterCustomArmorRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedCustomArmorsRepository cpRedCustomArmorsRepository;

    public Map<String, Object> getCharacterCustomArmors(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterCustomArmorDTO> characterCustomArmor = cpRedCharacterCustomArmorRepository.findAllByCharacterId(character)
                .stream()
                .map(armors -> new CpRedCharacterCustomArmorDTO(
                                armors.getId(),
                                armors.getArmorId().getId(),
                                armors.getCharacterId().getId(),
                                armors.getStatus().toString(),
                                armors.getPlace().toString(),
                                armors.getCurrentArmorPoints(),
                                armors.getDescription()
                        )
                )
                .toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe pancerze postaci pobrane pomyślnie");
        response.put("characterCustomArmors", characterCustomArmor);
        return response;
    }

    public Map<String,Object> createCharacterCustomArmor(AddCharacterCustomArmorRequest addCharacterCustomArmorRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        if (addCharacterCustomArmorRequest.getArmorId() == null ||
                addCharacterCustomArmorRequest.getCharacterId() == null ||
                addCharacterCustomArmorRequest.getStatus()==null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterCustomArmorRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        CpRedCustomArmors armor = cpRedCustomArmorsRepository.findById(addCharacterCustomArmorRequest.getArmorId())
                .orElseThrow(() -> new ResourceNotFoundException("Pancerz o podanym ID nie został znaleziony."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        if (!Objects.equals(character.getGame().getId(), armor.getGameId().getId())) {
            throw new ResourceNotFoundException("Postać nie należy do gry, do której jest przypisany customowy pancerz.");
        }

        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może dodawać customowe pancerze postaci NPC.");
            }
        }

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        if (addCharacterCustomArmorRequest.getStatus() == CpRedCharacterItemStatus.EQUIPPED){
            List<CpRedCharacterCustomArmor> placedArmors = cpRedCharacterCustomArmorRepository.findAllByCharacterIdAndPlace(character, addCharacterCustomArmorRequest.getPlace());
            // Sprawdzenie miejsca pancerza
            for (CpRedCharacterCustomArmor placedArmor : placedArmors) {
                if (placedArmor.getStatus() == CpRedCharacterItemStatus.EQUIPPED && placedArmor.getPlace() == addCharacterCustomArmorRequest.getPlace()) {
                    throw new IllegalArgumentException("Nie można mieć założonych więcej niż jednego pancerza w tym miejscu.");
                }
            }
        }


        CpRedCharacterCustomArmor newCharacterCustomArmor = new CpRedCharacterCustomArmor(
                null,
                armor,
                character,
                addCharacterCustomArmorRequest.getStatus(),
                addCharacterCustomArmorRequest.getPlace(),
                armor.getArmorPoints(),
                armor.getDescription()
        );

        cpRedCharacterCustomArmorRepository.save(newCharacterCustomArmor);

        return CustomReturnables.getOkResponseMap("Customowy pancerz został dodany do postaci.");
    }

public Map<String,Object> updateCharacterCustomArmor(Long characterCustomArmorId, UpdateCharacterCustomArmorRequest updateCharacterCustomArmorRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();
    User currentUser = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

    CpRedCharacterCustomArmor characterCustomArmorToUpdate = cpRedCharacterCustomArmorRepository.findById(characterCustomArmorId)
            .orElseThrow(() -> new ResourceNotFoundException("Pancerz postaci o podanym ID nie został znaleziony."));

    CpRedCharacters character = cpRedCharactersRepository.findById(characterCustomArmorToUpdate.getCharacterId().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

    CpRedCustomArmors armor = cpRedCustomArmorsRepository.findById(characterCustomArmorToUpdate.getArmorId().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Pancerz o podanym ID nie został znaleziony."));

    Game game = gameRepository.findById(character.getGame().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

    GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

    if (!Objects.equals(character.getGame().getId(), armor.getGameId().getId())) {
        throw new ResourceNotFoundException("Postać nie należy do gry, do której jest przypisany customowy pancerz.");
    }

    if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
        if (character.getType() != CpRedCharactersType.NPC) {
            if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
            }
        } else {
            throw new ResourceNotFoundException("Tylko GM może zmieniać customowe pancerze postaci NPC.");
        }
    }

    if (game.getStatus() != GameStatus.ACTIVE) {
        throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
    }

    if (updateCharacterCustomArmorRequest.getCurrentArmorPoints() != null){
        if (updateCharacterCustomArmorRequest.getCurrentArmorPoints() < 0) {
            throw new IllegalArgumentException("Aktualne punkty pancerza nie mogą być mniejsze niż 0.");
        }
        if (updateCharacterCustomArmorRequest.getCurrentArmorPoints() > armor.getArmorPoints()) {
            throw new IllegalArgumentException("Aktualne punkty pancerza nie mogą być większe niż maksymalne punkty pancerza.");
        }
        characterCustomArmorToUpdate.setCurrentArmorPoints(updateCharacterCustomArmorRequest.getCurrentArmorPoints());
    }

    if (updateCharacterCustomArmorRequest.getStatus() != null){
        if (updateCharacterCustomArmorRequest.getStatus() == CpRedCharacterItemStatus.EQUIPPED){
            List<CpRedCharacterCustomArmor> placedArmors = cpRedCharacterCustomArmorRepository.findAllByCharacterIdAndPlace(character, characterCustomArmorToUpdate.getPlace());
            for (CpRedCharacterCustomArmor placedArmor : placedArmors) {
                if (placedArmor.getStatus() == CpRedCharacterItemStatus.EQUIPPED && placedArmor.getPlace() == characterCustomArmorToUpdate.getPlace()) {
                    throw new IllegalArgumentException("Nie można mieć założonych więcej niż jednego pancerza w tym miejscu.");
                }
            }
        }
        characterCustomArmorToUpdate.setStatus(updateCharacterCustomArmorRequest.getStatus());
    }

    if (updateCharacterCustomArmorRequest.getDescription() != null) {
        if (updateCharacterCustomArmorRequest.getDescription().length() > 500) {
            throw new IllegalArgumentException("Opis nie może być dłuższy niż 500 znaków.");
        }
        characterCustomArmorToUpdate.setDescription(updateCharacterCustomArmorRequest.getDescription());
    }

    cpRedCharacterCustomArmorRepository.save(characterCustomArmorToUpdate);

    return CustomReturnables.getOkResponseMap("Customowy pancerz postaci został zmodyfikowany.");
    }

    public Map<String, Object> deleteCharacterCustomArmor(Long characterCustomArmorId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCharacterCustomArmor characterCustomArmor = cpRedCharacterCustomArmorRepository.findById(characterCustomArmorId)
                .orElseThrow(() -> new ResourceNotFoundException("Pancerz postaci o podanym ID nie został znaleziony."));

        CpRedCharacters character = cpRedCharactersRepository.findById(characterCustomArmor.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        CpRedCustomArmors armor = cpRedCustomArmorsRepository.findById(characterCustomArmor.getArmorId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pancerz o podanym ID nie został znaleziony."));

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
                throw new ResourceNotFoundException("Tylko GM może usuwać customowe pancerze postaci NPC.");
            }
        }

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        cpRedCharacterCustomArmorRepository.deleteById(characterCustomArmorId);
        return CustomReturnables.getOkResponseMap("Customowy pancerz postaci został pomyślnie usunięty.");
    }

    public Map<String, Object> getAllCharacterCustomArmors() {
        List<CpRedCharacterCustomArmor> allCharacterCustomArmors = cpRedCharacterCustomArmorRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie customowe pancerze postaci zostały pobrane pomyślnie.");
        response.put("characterCustomArmors", allCharacterCustomArmors);
        return response;
    }
}
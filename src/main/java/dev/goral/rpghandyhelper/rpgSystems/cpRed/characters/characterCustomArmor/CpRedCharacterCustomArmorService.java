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
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon.CpRedCharacterCustomWeaponDTO;
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
    private final dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon.CpRedCharacterCustomArmorRepository cpRedCharacterCustomArmorRepository;

    public Map<String, Object> getCharacterCustomArmors(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterCustomArmorDTO> characterCustomArmor = cpRedCharacterCustomArmorRepository.findAllByCharacter(character)
                .stream()
                .map(armors -> new CpRedCharacterCustomArmorDTO(
                                armors.getId(),
                                armors.getArmorId().getId(),
                                armors.getCharacterId().getId(),
                                armors.getStatus().toString(),
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

        CpRedCharacterCustomArmor newCharacterCustomArmor = new CpRedCharacterCustomArmor(
                null,
                armor,
                character,
                addCharacterCustomArmorRequest.getStatus(),
                armor.getArmorPoints(),
                armor.getDescription()
        );

        cpRedCharacterCustomArmorRepository.save(newCharacterCustomArmor);

        return CustomReturnables.getOkResponseMap("Customowy pancerz został dodany do postaci.");
    }



}
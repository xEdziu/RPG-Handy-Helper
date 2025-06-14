package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomArmor;

import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon.CpRedCharacterCustomWeaponDTO;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customArmors.CpRedCustomArmors;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customArmors.CpRedCustomArmorsRepository;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
}
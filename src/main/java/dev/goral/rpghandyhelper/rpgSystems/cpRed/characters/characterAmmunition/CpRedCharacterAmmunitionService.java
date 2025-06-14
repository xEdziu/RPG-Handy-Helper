package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition;

import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon.CpRedCharacterCustomWeaponRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeaponRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.ammunition.CpRedAmmunitionCompatibilityRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunitionRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition.CpRedAmmunitionRepository;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

        return CustomReturnables.getOkResponseMap("Amunicja została dodana do postaci");
    }

    public Map<String, Object> updateCharacterAmmunition(Long characterAmmunitionId,
                                                         UpdateCharacterAmmunitionRequest updateCharacterAmmunitionRequest) {

        return CustomReturnables.getOkResponseMap("Amunicja postaci została pomyślnie zmodyfikowana");
    }

    public Map<String, Object> deleteCharacterAmmunition(Long characterAmmunitionId) {
        return CustomReturnables.getOkResponseMap("Amunicja postaci została pomyślnie usunięta");
    }


    // ============ Admin methods ============
    public Map<String, Object> getAllCharacterAmmunition() {
        List<CpRedCharacterAmmunition> allCharacterAmmunition = characterAmmunitionRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie amunicje postaci pobrane pomyślnie");
        response.put("allCharacterAmmunition", allCharacterAmmunition);
        return response;
    }
}

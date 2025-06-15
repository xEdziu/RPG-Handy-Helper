package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware;

import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware.CpRedCharacterCustomCyberwareRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCyberwares.CpRedCustomCyberwaresRepository;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;

@Service
@AllArgsConstructor
public class CpRedCharacterCustomCyberwareService {
    private final CpRedCharacterCustomCyberwareRepository cpRedCharacterCustomCyberwareRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedCustomCyberwaresRepository cpRedCustomCyberwaresRepository;

    public Map<String, Object> getCharacterCustomCyberwares(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterCustomCyberwareDTO> characterCustomCyberware = cpRedCharacterCustomCyberwareRepository.findAllByCharacterId(character)
                .stream()
                .map( cyberwares -> new CpRedCharacterCustomCyberwareDTO(
                        cyberwares.getId(),
                        cyberwares.getCyberwareId().getId(),
                        cyberwares.getCharacterId().getId(),
                        cyberwares.getStatus().toString(),
                        cyberwares.getDescription()
                    )
                ).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe wszczepy postaci pobrane pomyślnie.");
        response.put("characterCustomCyberwares", characterCustomCyberware);
        return response;
    }
}

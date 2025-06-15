package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware.AddCharacterCustomCyberwareRequest;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware.CpRedCharacterCustomCyberware;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware.CpRedCharacterCustomCyberwareRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCyberwares.CpRedCustomCyberwares;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCyberwares.CpRedCustomCyberwaresRepository;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.Objects;

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

    public Map<String,Object> createCharacterCustomCyberware(AddCharacterCustomCyberwareRequest addCharacterCustomCyberwareRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        if (addCharacterCustomCyberwareRequest.getCyberwareId() == null ||
                addCharacterCustomCyberwareRequest.getCharacterId() == null ||
                addCharacterCustomCyberwareRequest.getStatus()==null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterCustomCyberwareRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        CpRedCustomCyberwares cyberware = cpRedCustomCyberwaresRepository.findById(addCharacterCustomCyberwareRequest.getCyberwareId())
                .orElseThrow(() -> new ResourceNotFoundException("Wszczep o podanym ID nie został znaleziony."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        if (!Objects.equals(character.getGame().getId(), cyberware.getGameId().getId())) {
            throw new ResourceNotFoundException("Postać nie należy do gry, do której jest przypisany customowy wszczep.");
        }

        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może dodawać customowe wszczepy postaci NPC.");
            }
        }

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        CpRedCharacterCustomCyberware newCharacterCustomCyberware = new CpRedCharacterCustomCyberware(
                null,
                cyberware,
                character,
                addCharacterCustomCyberwareRequest.getStatus(),
                cyberware.getDescription()
        );

        cpRedCharacterCustomCyberwareRepository.save(newCharacterCustomCyberware);

        return CustomReturnables.getOkResponseMap("Customowy wszczep został dodany do postaci.");
    }
}

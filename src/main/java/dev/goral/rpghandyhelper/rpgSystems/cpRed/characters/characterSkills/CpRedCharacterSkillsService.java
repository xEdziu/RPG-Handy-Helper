package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterSkills;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.skills.CpRedSkills;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.skills.CpRedSkillsCategory;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.skills.CpRedSkillsRepository;
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
public class CpRedCharacterSkillsService {
    private final CpRedCharacterSkillsRepository cpRedCharacterSkillsRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedSkillsRepository cpRedSkillsRepository;

    public Map<String, Object> getCharacterSkills(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterSkillsDTO> characterSkills = cpRedCharacterSkillsRepository.getCharacterSkillsByCharacter(character)
                .stream()
                .map(skill -> new CpRedCharacterSkillsDTO(
                        skill.getId(),
                        skill.getCharacter().getId(),
                        skill.getSkill().getId(),
                        skill.getSkillLevel()))
                .toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Umiejętności postaci pobrane pomyślnie");
        response.put("characterSkills", characterSkills);
        return response;
    }

    public Map<String, Object> createCharacterSkill(AddCharacterSkillsRequest addCharacterSkillsRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy podano wszystkie wymagane pola w request
        if (addCharacterSkillsRequest.getCharacterId() == null || addCharacterSkillsRequest.getSkillId() == null ||
            addCharacterSkillsRequest.getSkillLevel() < 0) {
            throw new IllegalArgumentException("Nie podano wszystkich wymaganych pól.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterSkillsRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje skill o podanym ID
        CpRedSkills skill = cpRedSkillsRepository.findById(addCharacterSkillsRequest.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Umiejętność o podanym ID nie została znaleziona."));

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
                throw new ResourceNotFoundException("Tylko GM może dodawać umiejętności postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Czy już istnieje umiejętność postaci o podanym ID dla tej postaci
        if( cpRedCharacterSkillsRepository.existsByCharacterAndSkill(character, skill)) {
            throw new IllegalArgumentException("Postać już posiada tę umiejętność.");
        }

        if (addCharacterSkillsRequest.getSkillLevel() < 0) {
            throw new IllegalArgumentException("Poziom umiejętności nie może być mniejszy niż 0.");
        }
        if (addCharacterSkillsRequest.getSkillLevel() > 30) {
            throw new IllegalArgumentException("Poziom umiejętności nie może być większy niż 30.");
        }

        CpRedCharacterSkills newCharacterSkill = new CpRedCharacterSkills(
                null,
                character,
                skill,
                addCharacterSkillsRequest.getSkillLevel()
        );

        cpRedCharacterSkillsRepository.save(newCharacterSkill);
        return CustomReturnables.getOkResponseMap("Umiejętność została dodana do postaci");
    }

    public Map<String, Object> updateCharacterSkill(Long characterSkillId,
                                                    UpdateCharacterSkillsRequest updateCharacterSkillsRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje characterSkill o podanym ID
        CpRedCharacterSkills characterSkillToUpdate = cpRedCharacterSkillsRepository.findById(characterSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("Umiejętność postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterSkillToUpdate.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje skill o podanym ID
        CpRedSkills skill = cpRedSkillsRepository.findById(characterSkillToUpdate.getSkill().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Umiejętność o podanym ID nie została znaleziona."));

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
                throw new ResourceNotFoundException("Tylko GM może dodawać umiejętności postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        if (updateCharacterSkillsRequest.getSkillLevel() != null) {
            if (updateCharacterSkillsRequest.getSkillLevel() < 0) {
                throw new IllegalArgumentException("Poziom umiejętności nie może być mniejszy niż 0.");
            }
            if (updateCharacterSkillsRequest.getSkillLevel() > 30) {
                throw new IllegalArgumentException("Poziom umiejętności nie może być większy niż 30.");
            }
            characterSkillToUpdate.setSkillLevel(updateCharacterSkillsRequest.getSkillLevel());
        }

        cpRedCharacterSkillsRepository.save(characterSkillToUpdate);
        return CustomReturnables.getOkResponseMap("Umiejętność postaci została zaktualizowana pomyślnie");
    }

    public Map<String, Object> deleteCharacterSkill(Long characterSkillId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje characterSkill o podanym ID
        CpRedCharacterSkills characterSkillToDelete = cpRedCharacterSkillsRepository.findById(characterSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("Umiejętność postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterSkillToDelete.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje skill o podanym ID
        CpRedSkills skill = cpRedSkillsRepository.findById(characterSkillToDelete.getSkill().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Umiejętność o podanym ID nie została znaleziona."));

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
                throw new ResourceNotFoundException("Tylko GM może dodawać umiejętności postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        cpRedCharacterSkillsRepository.deleteById(characterSkillId);
        return CustomReturnables.getOkResponseMap("Umiejętność postaci została usunięta pomyślnie");
    }

    public Map<String, Object> getAllCharactersSkills() {
        List<CpRedCharacterSkills> allCharacterSkills = cpRedCharacterSkillsRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie umiejętności każdej postaci pobrane pomyślnie");
        response.put("characterSkills", allCharacterSkills);
        return response;
    }

    public List<CpRedCharacterSkillsSheetDTO> getCharacterSkills(Long characterId, CpRedSkillsCategory category) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        return cpRedCharacterSkillsRepository.getCharacterSkillsByCharacterAndCategory(character, category)
                .stream()
                .map(skill -> new CpRedCharacterSkillsSheetDTO(
                        skill.getId(),
                        skill.getSkill().getId(),
                        skill.getSkill().getName() + " (" + skill.getSkill().getConnectedStat().getTag() + ")",
                        skill.getSkillLevel(),
                        skill.getSkill().getCategory().toString()))
                .toList();
    }

}

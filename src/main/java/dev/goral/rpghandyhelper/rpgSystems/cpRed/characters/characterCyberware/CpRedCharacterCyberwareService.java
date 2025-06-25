package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCyberware;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware.CpRedCharacterCustomCyberware;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware.CpRedCharacterCustomCyberwareRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.cyberwares.CpRedCyberwares;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.cyberwares.CpRedCyberwaresMountPlace;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.cyberwares.CpRedCyberwaresRepository;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CpRedCharacterCyberwareService {
    private CpRedCharacterCyberwareRepository cpRedCharacterCyberwareRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedCyberwaresRepository cpRedCyberwaresRepository;
    private final CpRedCharacterCustomCyberwareRepository cpRedCharacterCustomCyberwareRepository;

    public Map<String, Object> getCharacterCyberwares(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterCyberwareDTO> characterCyberwares = cpRedCharacterCyberwareRepository.findByCharacter(character)
                .stream()
                .map(cyberware -> new CpRedCharacterCyberwareDTO(
                        cyberware.getId(),
                        cyberware.getBaseCyberware().getId(),
                        cyberware.getCharacter().getId(),
                        cyberware.getDescription()
                        )
                )
                .toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszczepy postaci pobrane pomyślnie");
        response.put("characterCyberwares", characterCyberwares);
        return response;
    }

    public Map<String, Object> createCharacterCyberware(AddCharacterCyberwareRequest addCharacterCyberwareRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy podano wszystkie wymagane pola w request
        if (addCharacterCyberwareRequest.getCharacterId() == null ||
            addCharacterCyberwareRequest.getBaseCyberwareId() == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterCyberwareRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje wszczep o podanym ID
        CpRedCyberwares cyberware = cpRedCyberwaresRepository.findById(addCharacterCyberwareRequest.getBaseCyberwareId())
                .orElseThrow(() -> new ResourceNotFoundException("Wszczep o podanym ID nie został znaleziony."));

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
                throw new ResourceNotFoundException("Tylko GM może dodawać wszczepy postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        CpRedCharacterCyberware newCharacterCyberware = new CpRedCharacterCyberware(
                null,
                cyberware,
                character,
                cyberware.getDescription()
        );

        cpRedCharacterCyberwareRepository.save(newCharacterCyberware);
        return CustomReturnables.getOkResponseMap("Wszczep został dodany do postaci");
    }

    public Map<String, Object> updateCharacterCyberware(Long characterCyberwareId,
                                                        UpdateCharacterCyberwareRequest updateCharacterCyberwareRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje wszczep o podanym ID
        CpRedCharacterCyberware characterCyberwareToUpdate = cpRedCharacterCyberwareRepository.findById(characterCyberwareId)
                .orElseThrow(() -> new ResourceNotFoundException("Wszczep postaci o podanym ID nie został znaleziony."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterCyberwareToUpdate.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje broń o podanym ID
        CpRedCyberwares cyberware = cpRedCyberwaresRepository.findById(characterCyberwareToUpdate.getBaseCyberware().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wszczep o podanym ID nie został znaleziony."));

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
                throw new ResourceNotFoundException("Tylko GM może zmieniać wszczepy postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        if (updateCharacterCyberwareRequest.getDescription() != null) {
            if (updateCharacterCyberwareRequest.getDescription().length() > 1000) {
                throw new IllegalArgumentException("Opis wszczepu nie może być dłuższy niż 1000 znaków.");
            }
            characterCyberwareToUpdate.setDescription(updateCharacterCyberwareRequest.getDescription());
        }

        cpRedCharacterCyberwareRepository.save(characterCyberwareToUpdate);

        return CustomReturnables.getOkResponseMap("Wszczep postaci został zaktualizowany pomyślnie");
    }

    public Map<String, Object> deleteCharacterCyberware(Long characterCyberwareId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje wszczep o podanym ID
        CpRedCharacterCyberware characterCyberwareToDelete = cpRedCharacterCyberwareRepository.findById(characterCyberwareId)
                .orElseThrow(() -> new ResourceNotFoundException("Wszczep postaci o podanym ID nie został znaleziony."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterCyberwareToDelete.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje broń o podanym ID
        CpRedCyberwares cyberware = cpRedCyberwaresRepository.findById(characterCyberwareToDelete.getBaseCyberware().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wszczep o podanym ID nie został znaleziony."));

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
                throw new ResourceNotFoundException("Tylko GM może usuwać wszczepy postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        cpRedCharacterCyberwareRepository.delete(characterCyberwareToDelete);

        return CustomReturnables.getOkResponseMap("Wszczep postaci został usunięty pomyślnie");
    }

    public Map<String, Object> getAllCharacterCyberwares() {
        List<CpRedCharacterCyberware> allCharacterCyberwares = cpRedCharacterCyberwareRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie wszczepy postaci pobrane pomyślnie");
        response.put("allCharacterCyberware", allCharacterCyberwares);
        return response;
    }

    public List<CpRedCharacterCyberwareSheetDTO> getCharacterCyberwareForSheet(Long characterId, CpRedCyberwaresMountPlace mountPlace){
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterCyberware> characterManualCyberwareList = cpRedCharacterCyberwareRepository.findAllByCharacter(character);
        List<CpRedCharacterCustomCyberware> characterCustomCyberwareList = cpRedCharacterCustomCyberwareRepository.findAllByCharacterId(character);
        List<CpRedCharacterCyberwareSheetDTO> characterCyberwareDTO = new ArrayList<>();
        for(CpRedCharacterCyberware cyberware : characterManualCyberwareList){
            if(cyberware.getBaseCyberware().getMountPlace() == mountPlace){
                CpRedCharacterCyberwareSheetDTO dto = new CpRedCharacterCyberwareSheetDTO(
                        cyberware.getId(),
                        cyberware.getBaseCyberware().getId(),
                        false,
                        cyberware.getBaseCyberware().getName(),
                        cyberware.getDescription(),
                        cyberware.getBaseCyberware().getMountPlace().toString()
                );
                characterCyberwareDTO.add(dto);
            }
        }
        for(CpRedCharacterCustomCyberware customCyberware : characterCustomCyberwareList){
            if(customCyberware.getCyberwareId().getMountPlace() == mountPlace){
                CpRedCharacterCyberwareSheetDTO dto = new CpRedCharacterCyberwareSheetDTO(
                        customCyberware.getId(),
                        customCyberware.getCyberwareId().getId(),
                        true,
                        customCyberware.getCyberwareId().getName(),
                        customCyberware.getDescription(),
                        customCyberware.getCyberwareId().getMountPlace().toString()
                );
                characterCyberwareDTO.add(dto);
            }
        }
        return characterCyberwareDTO;
    }
}

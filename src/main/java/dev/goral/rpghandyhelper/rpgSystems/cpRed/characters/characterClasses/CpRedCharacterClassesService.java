package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterClasses;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.classes.CpRedClasses;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.classes.CpRedClassesRepository;
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
public class CpRedCharacterClassesService {
    private final CpRedCharacterClassesRepository cpRedCharacterClassesRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final CpRedClassesRepository cpRedClassesRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;

    public Map<String, Object> getCharacterClasses(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterClassesDTO> characterClasses = cpRedCharacterClassesRepository.getCharacterClassesByCharacterId(character).stream()
                .map(characterClass -> new CpRedCharacterClassesDTO(
                        characterClass.getId(),
                        characterClass.getClassLevel(),
                        characterClass.getCharacterId().getId(),
                        characterClass.getClassId().getId()))
                .toList();


        Map<String, Object> response = CustomReturnables.getOkResponseMap("Klasy postaci pobrane pomyślnie");
        response.put("characterClass", characterClasses);
        return response;
    }

    public Map<String, Object> createCharacterClass(AddCharacterClassesRequest addCharacterClassesRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy podano wszystkie wymagane pola w request
        if (addCharacterClassesRequest.getCharacterId() == null || addCharacterClassesRequest.getClassId() == null || addCharacterClassesRequest.getClassLevel() == -1) {
            throw new ResourceNotFoundException("Nie podano wszystkich wymaganych pól.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterClassesRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje klasa o podanym ID
        CpRedClasses characterClass = cpRedClassesRepository.findById(addCharacterClassesRequest.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Klasa o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        // Cy użytkownik należy do gry, do której należy postać
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        // Czy ktoś chce zmieniać swoją postać lub jest GM-em w tej grze
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może zmieniać statystyki postaci NPC.");
            }
        }

        // Sprawdź, czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Sprawdzenie, czy klasa już istnieje dla tej postaci
        if (cpRedCharacterClassesRepository.existsByCharacterIdAndClassId(character, characterClass)) {
            throw new ResourceNotFoundException("Klasa o podanym ID już istnieje dla tej postaci.");
        }

        // Czy poziom klasy jest większy niż 0 i mniejszy równy 10
        if (addCharacterClassesRequest.getClassLevel() <= 0 || addCharacterClassesRequest.getClassLevel() > 10) {
            throw new ResourceNotFoundException("Poziom klasy musi być z zakresu od 1 do 10.");
        }

        // Tworzenie nowej klasy postaci
        CpRedCharacterClasses newCharacterClass = new CpRedCharacterClasses(
                null,
                addCharacterClassesRequest.getClassLevel(),
                character,
                characterClass
        );

        cpRedCharacterClassesRepository.save(newCharacterClass);
        return CustomReturnables.getOkResponseMap("Klasa postaci została pomyślnie utworzona");
    }

    public Map<String, Object> updateCharacterClass(Long characterClassId, UpdateCharacterClassesRequest updateCharacterClassesRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy podana klasa postaci o podanym ID istnieje
        CpRedCharacterClasses characterClassToUpdate = cpRedCharacterClassesRepository.findById(characterClassId)
                .orElseThrow(() -> new ResourceNotFoundException("Klasa postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterClassToUpdate.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        // Cy użytkownik należy do gry, do której należy postać
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        // Czy ktoś chce zmieniać swoją postać lub jest GM-em w tej grze
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może zmieniać statystyki postaci NPC.");
            }
        }

        // Sprawdź, czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        if (updateCharacterClassesRequest.getClassLevel() != -1){
            // Czy poziom klasy jest większy niż 0 i mniejszy równy 10
            if (updateCharacterClassesRequest.getClassLevel() <= 0 || updateCharacterClassesRequest.getClassLevel() > 10) {
                throw new ResourceNotFoundException("Poziom klasy musi być z zakresu od 1 do 10.");
            }
            characterClassToUpdate.setClassLevel(updateCharacterClassesRequest.getClassLevel());
        }

        cpRedCharacterClassesRepository.save(characterClassToUpdate);
        return CustomReturnables.getOkResponseMap("Klasa postaci została pomyślnie zaktualizowana");
    }

    public Map<String, Object> deleteCharacterClass(Long characterClassId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        // Czy podana klasa postaci o podanym ID istnieje
        CpRedCharacterClasses characterClassToUpdate = cpRedCharacterClassesRepository.findById(characterClassId)
                .orElseThrow(() -> new ResourceNotFoundException("Klasa postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterClassToUpdate.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        // Cy użytkownik należy do gry, do której należy postać
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        // Czy ktoś chce zmieniać swoją postać lub jest GM-em w tej grze
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może zmieniać statystyki postaci NPC.");
            }
        }

        // Sprawdź, czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Usuwanie klasy postaci
        cpRedCharacterClassesRepository.deleteById(characterClassId);

        return CustomReturnables.getOkResponseMap("Klasa postaci została pomyślnie usunięta");
    }

    public Map<String, Object> getAllCharactersClasses() {
        List<CpRedCharacterClasses> allCharacterClasses = cpRedCharacterClassesRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie klasy każdej postaci pobrane pomyślnie");
        response.put("characterClass", allCharacterClasses);
        return response;
    }
}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterClasses;

import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
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

@Service
@AllArgsConstructor
public class CpRedCharacterClassesService {
    private final CpRedCharacterClassesRepository cpRedCharacterClassesRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;

    public Map<String, Object> getCharacterClasses(Long characterId) {
        List<CpRedCharacterClassesDTO> characterClasses = cpRedCharacterClassesRepository.getCharacterClassesByCharacterId(characterId).stream()
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

        // Czy użytkownik jest GM-em w grze, do której należy postać
        // Czy ktoś chce zmieniać swoją postać lub jest GM-em w tej grze
        // Czy podano wszystkie wymagane pola w request
        // Czy istnieje character o podanym ID
        // Czy istnieje klasa o podanym ID
        // Czy poziom klasy jest większy niż 0 i mniejszy równy 10


        return CustomReturnables.getOkResponseMap("Klasa postaci została pomyślnie utworzona");
    }

    public Map<String, Object> updateCharacterClass(Long characterClassId, AddCharacterClassesRequest addCharacterClassesRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        // Czy użytkownik jest GM-em w grze, do której należy postać
        // Czy ktoś chce zmieniać swoją postać lub jest GM-em w tej grze
        // Czy istnieje character o podanym ID
        // Czy istnieje klasa o podanym ID
        // Czy poziom klasy jest większy niż 0 i mniejszy równy 10

        return CustomReturnables.getOkResponseMap("Klasa postaci została pomyślnie zaktualizowana");
    }

    public Map<String, Object> deleteCharacterClass(Long characterClassId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        // Czy użytkownik jest GM-em w grze, do której należy postać
        // Czy ktoś chce zmieniać swoją postać lub jest GM-em w tej grze
        // Czy istnieje klasa charactera o podanym ID (czy masz co usunąć)

        return CustomReturnables.getOkResponseMap("Klasa postaci została pomyślnie usunięta");
    }

    public Map<String, Object> getAllCharactersClasses() {
        List<CpRedCharacterClasses> allCharacterClasses = cpRedCharacterClassesRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie klasy każdej postaci pobrane pomyślnie");
        response.put("characterClass", allCharacterClasses);
        return response;
    }
}

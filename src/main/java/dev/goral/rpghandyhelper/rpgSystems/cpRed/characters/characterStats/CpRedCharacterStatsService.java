package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterStats;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.stats.CpRedStats;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.stats.CpRedStatsRepository;
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
public class CpRedCharacterStatsService {
    private final CpRedCharacterStatsRepository cpRedCharacterStatsRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final CpRedStatsRepository cpRedStatsRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;

    public Map<String, Object> getCharacterStats(Long characterId){
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterStatsDTO> characterStats = cpRedCharacterStatsRepository.getCharacterStatsByCharacterId(character).stream()
                .map(stats -> new CpRedCharacterStatsDTO(
                        stats.getMaxStatLevel(),
                        stats.getCurrentStatLevel(),
                        stats.getCharacterId().getId(),
                        stats.getStatId().getId()))
                .toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Statystyki postaci pobrane pomyślnie");
        response.put("characterStats", characterStats);
        return response;
    }

    public Map<String, Object> createCharacterStats(AddCharacterStatsRequest addCharacterStatsRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy podano wszystkie wymagane pola w request
        if (addCharacterStatsRequest.getMaxStatLevel() == null || addCharacterStatsRequest.getCurrentStatLevel() == null ||
                addCharacterStatsRequest.getCharacterId() == null || addCharacterStatsRequest.getStatId() == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterStatsRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje statystyka o podanym ID
        CpRedStats stat = cpRedStatsRepository.findById(addCharacterStatsRequest.getStatId())
                .orElseThrow(() -> new ResourceNotFoundException("Statystyka o podanym ID nie została znaleziona."));

        // Sprawdzenie, czy statystyka już istnieje dla tej postaci
        if (cpRedCharacterStatsRepository.existsByCharacterIdAndStatId(character, stat)) {
            throw new IllegalArgumentException("Statystyka o podanym ID już istnieje dla tej postaci.");
        }

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
                throw new ResourceNotFoundException("Tylko GM może zmieniać statystyki postaci NPC.");
            }
        }

        // Czy maksymalna statystyka jest potrzebna
        if (stat.isChangeable()){
            // Czy maksymalny poziom statystyki jet większy od -1 i mniejsza od 21
            if (addCharacterStatsRequest.getMaxStatLevel() < 0 || addCharacterStatsRequest.getMaxStatLevel() > 20) {
                throw new IllegalArgumentException("Maksymalny poziom statystyki musi być z zakresu od 0 do 20.");
            }
            // Czy aktualny poziom statystyki jet większy od -1 i mniejsza od 21
            if (addCharacterStatsRequest.getCurrentStatLevel() < 0 || addCharacterStatsRequest.getCurrentStatLevel() > 20) {
                throw new IllegalArgumentException("Aktualny poziom statystyki musi być z zakresu od 0 do 20");
            }
            // Czy aktualny poziom statystyki jest nie większy od maksymalnego poziomu statystyki
            if (addCharacterStatsRequest.getCurrentStatLevel() > addCharacterStatsRequest.getMaxStatLevel()) {
                throw new IllegalArgumentException("Aktualny poziom statystyki nie może być większy niż maksymalny poziom statystyki.");
            }
        } else {
            // Czy aktualny poziom statystyki jet większy od -1 i mniejsza od 21
            if (addCharacterStatsRequest.getCurrentStatLevel() < 0 || addCharacterStatsRequest.getCurrentStatLevel() > 20) {
                throw new IllegalArgumentException("Aktualny poziom statystyki musi być z zakresu od 0 do 20");
            }
        }

        // Tworzenie nowej klasy postaci
        CpRedCharacterStats newCharacterStats = new CpRedCharacterStats(
                null,
                addCharacterStatsRequest.getMaxStatLevel(),
                addCharacterStatsRequest.getCurrentStatLevel(),
                character,
                stat
        );
        cpRedCharacterStatsRepository.save(newCharacterStats);

        return CustomReturnables.getOkResponseMap("Statystyka postaci została pomyślnie utworzona");
    }

    public Map<String, Object> updateCharacterStats(Long characterStatsId, UpdateCharacterStatsRequest updateCharacterStatsRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje statystyka o podanym ID
        CpRedCharacterStats characterStatsToUpdate = cpRedCharacterStatsRepository.findById(characterStatsId)
                .orElseThrow(() -> new ResourceNotFoundException("Statystyka postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterStatsToUpdate.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        // Czy użytkownik należy do gry, do której należy postać
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        CpRedStats stat = cpRedStatsRepository.findById(characterStatsToUpdate.getStatId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Powiązana statystyka nie została znaleziona."));

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

        // Czy statystyka jest zmienna
        if (!stat.isChangeable() && updateCharacterStatsRequest.getMaxStatLevel() != null){
            throw new IllegalArgumentException("Ta statystyka nie jest zmienna, więc nie możesz ustawić maksymalnego poziomu statystyki");
        }

        // Sprawdzenie maksymalnego poziomu statystyki
        if (updateCharacterStatsRequest.getMaxStatLevel() != null) {
            if (updateCharacterStatsRequest.getMaxStatLevel() < 0 || updateCharacterStatsRequest.getMaxStatLevel() > 20) {
                throw new IllegalArgumentException("Maksymalny poziom statystyki musi być z zakresu od 0 do 20.");
            }
            if (updateCharacterStatsRequest.getMaxStatLevel() < characterStatsToUpdate.getCurrentStatLevel()) {
                throw new IllegalArgumentException("Maksymalny poziom statystyki nie może być mniejszy niż aktualny poziom statystyki.");
            }
            characterStatsToUpdate.setMaxStatLevel(updateCharacterStatsRequest.getMaxStatLevel());
        }

        // Sprawdzenie aktualnego poziomu statystyki
        if (updateCharacterStatsRequest.getCurrentStatLevel() != null) {
            if (updateCharacterStatsRequest.getCurrentStatLevel() < 0 || updateCharacterStatsRequest.getCurrentStatLevel() > 20) {
                throw new IllegalArgumentException("Aktualny poziom statystyki musi być z zakresu od 0 do 20");
            }
            // Czy aktualny poziom statystyki jest nie większy od maksymalnego poziomu statystyki
            if (updateCharacterStatsRequest.getCurrentStatLevel() > characterStatsToUpdate.getMaxStatLevel()) {
                throw new IllegalArgumentException("Aktualny poziom statystyki nie może być większy niż maksymalny poziom statystyki.");
            }
            characterStatsToUpdate.setCurrentStatLevel(updateCharacterStatsRequest.getCurrentStatLevel());
        }

        cpRedCharacterStatsRepository.save(characterStatsToUpdate);
        return CustomReturnables.getOkResponseMap("Statystyka postaci została pomyślnie zaktualizowana");
    }

    public Map<String, Object> deleteCharacterStats(Long characterStatsId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje statystyka o podanym ID
        CpRedCharacterStats characterStatsToUpdate = cpRedCharacterStatsRepository.findById(characterStatsId)
                .orElseThrow(() -> new ResourceNotFoundException("Statystyka postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterStatsToUpdate.getCharacterId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

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
                throw new ResourceNotFoundException("Tylko GM może zmieniać statystyki postaci NPC.");
            }
        }

        // Usuwanie statystyki postaci
        cpRedCharacterStatsRepository.deleteById(characterStatsId);

        return CustomReturnables.getOkResponseMap("Statystyka postaci została pomyślnie usunięta");
    }

    public Map<String, Object> getAllCharacterStats() {
        List<CpRedCharacterStats> allCharacterStats = cpRedCharacterStatsRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie statystyki każdej postaci pobrane pomyślnie");
        response.put("characterStats", allCharacterStats);
        return response;
    }

}

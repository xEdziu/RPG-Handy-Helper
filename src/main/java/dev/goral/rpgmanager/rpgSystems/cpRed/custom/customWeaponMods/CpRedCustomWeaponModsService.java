package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeaponMods;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.game.GameStatus;
import dev.goral.rpgmanager.game.gameUsers.GameUsers;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRole;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customCyberwares.CpRedCustomCyberwares;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomWeaponModsService {
    private final CpRedCustomWeaponModsRepository cpRedCustomWeaponModsRepository;
    private final GameUsersRepository gameUsersRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;



    // Pobierz wszystkie modyfikacje broni
    public Map<String, Object> getAllWeaponMods() {
        List<CpRedCustomWeaponModsDTO> allCustomWeaponMods = cpRedCustomWeaponModsRepository.findAll().stream()
                .map(CpRedCustomWeaponMods -> new CpRedCustomWeaponModsDTO(
                        CpRedCustomWeaponMods.getGameId().getId(),
                        CpRedCustomWeaponMods.getName(),
                        CpRedCustomWeaponMods.getPrice(),
                        CpRedCustomWeaponMods.getSize(),
                        CpRedCustomWeaponMods.getAvailability().toString(),
                        CpRedCustomWeaponMods.getDescription()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe modyfikacje broni zostały pobrane.");
        response.put("customWeaponMods", allCustomWeaponMods);
        return response;
    }


    public Map<String, Object> getWeaponModById(Long weaponModId) {
        CpRedCustomWeaponModsDTO cpRedCustomWeaponMods = cpRedCustomWeaponModsRepository.findById(weaponModId)
                .map(CpRedCustomWeaponMods -> new CpRedCustomWeaponModsDTO(
                        CpRedCustomWeaponMods.getGameId().getId(),
                        CpRedCustomWeaponMods.getName(),
                        CpRedCustomWeaponMods.getPrice(),
                        CpRedCustomWeaponMods.getSize(),
                        CpRedCustomWeaponMods.getAvailability().toString(),
                        CpRedCustomWeaponMods.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Customowa modyfikacja o id " + weaponModId + " nie istnieje."));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowa modyfikacja broni została pobrana.");
        response.put("customWeaponMod", cpRedCustomWeaponMods);
        return response;
    }

    public Map<String, Object> getWeaponModsByGame(Long gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + gameId + " nie istnieje."));

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie jesteś graczem wybranej gry."));
        List<CpRedCustomWeaponModsDTO> allCustomMods=cpRedCustomWeaponModsRepository.findAllByGameId(game).stream()
                .map(CpRedCustomWeaponMods -> new CpRedCustomWeaponModsDTO(
                        CpRedCustomWeaponMods.getGameId().getId(),
                        CpRedCustomWeaponMods.getName(),
                        CpRedCustomWeaponMods.getPrice(),
                        CpRedCustomWeaponMods.getSize(),
                        CpRedCustomWeaponMods.getAvailability().toString(),
                        CpRedCustomWeaponMods.getDescription()
                )).toList();
        Map<String,Object> response= CustomReturnables.getOkResponseMap("Customowe modyfikacje do broni, do gry zostały pobrane.");
        response.put("customMods",allCustomMods);
        return response;
    }




    // Dodaj modyfikację broni
    public Map<String, Object> addWeaponMod(CpRedCustomWeaponModsRequest cpRedCustomWeaponMods) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if (cpRedCustomWeaponMods.getGameId() == null ||
                cpRedCustomWeaponMods.getName() == null ||
                cpRedCustomWeaponMods.getPrice() < 0 ||
                cpRedCustomWeaponMods.getSize() < 0 ||
                cpRedCustomWeaponMods.getAvailability() == null ||
                cpRedCustomWeaponMods.getDescription() == null) {
            throw new IllegalStateException("Wszystkie pola muszą być wypełnione.");
        }
        Game game = gameRepository.findById(cpRedCustomWeaponMods.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + cpRedCustomWeaponMods.getGameId() + " nie istnieje."));
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), cpRedCustomWeaponMods.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));

        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może dodać modyfikację do gry.");
        }
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + cpRedCustomWeaponMods.getGameId() + " nie jest aktywna.");
        }
        if (cpRedCustomWeaponModsRepository.existsByNameAndGameId(cpRedCustomWeaponMods.getName(), game)) {
            throw new IllegalStateException("Customowa modyfikacja broni o tej nazwie już istnieje.");
        }
        if (cpRedCustomWeaponMods.getName().isEmpty() ||
                cpRedCustomWeaponMods.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa modyfikacji nie może być pusta.");
        }
        if (cpRedCustomWeaponMods.getName().length() > 255) {
            throw new IllegalStateException("Nazwa modyfikacji nie może być dłuższa niż 255 znaków.");
        }
        if (cpRedCustomWeaponMods.getDescription().isEmpty() ||
                cpRedCustomWeaponMods.getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Opis modyfikacji nie może być pusty.");
        }
        if (cpRedCustomWeaponMods.getDescription().length() > 500) {
            throw new IllegalStateException("Opis modyfikacji nie może być dłuższy niż 500 znaków.");
        }
        if (cpRedCustomWeaponMods.getSize() < 0) {
            throw new IllegalStateException("Rozmiar modyfikacji nie może być mniejszy lub równy 0.");
        }
        if (cpRedCustomWeaponMods.getPrice() < 0) {
            throw new IllegalStateException("Cena modyfikacji nie może być mniejsza lub równa 0.");
        }
        CpRedCustomWeaponMods newWeaponMod = new CpRedCustomWeaponMods(
                null,
                game,
                cpRedCustomWeaponMods.getName(),
                cpRedCustomWeaponMods.getPrice(),
                cpRedCustomWeaponMods.getSize(),
                cpRedCustomWeaponMods.getAvailability(),
                cpRedCustomWeaponMods.getDescription()
        );
        CpRedCustomWeaponMods savedWeaponMod = cpRedCustomWeaponModsRepository.save(newWeaponMod);
        return CustomReturnables.getOkResponseMap("Customowa modyfikacja broni została dodana.");
    }

    public Map<String, Object> updateWeaponMod(Long weaponModId, CpRedCustomWeaponMods cpRedCustomWeaponMods) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        CpRedCustomWeaponMods modToUpdate = cpRedCustomWeaponModsRepository.findById(weaponModId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowa modyfikacja o id " + weaponModId + " nie istnieje."));

        Game game = gameRepository.findById(modToUpdate.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id "+modToUpdate.getGameId().getId()+" nie istnieje."));
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + modToUpdate.getGameId().getId() + " nie jest aktywna.");
        }
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), modToUpdate.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może edytować wszczep w grze.");
        }
        if(cpRedCustomWeaponMods.getName()!=null){
            if (cpRedCustomWeaponModsRepository.existsByNameAndGameId(cpRedCustomWeaponMods.getName(), game)) {
                throw new IllegalStateException("Customowa modyfikacja broni o tej nazwie już istnieje.");
            }
            if (cpRedCustomWeaponMods.getName().isEmpty() ||
                    cpRedCustomWeaponMods.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa modyfikacji broni nie może być pusta.");
            }
            if (cpRedCustomWeaponMods.getName().length() > 255) {
                throw new IllegalStateException("Nazwa modyfikacji broni nie może być dłuższa niż 255 znaków.");
            }
            modToUpdate.setName(cpRedCustomWeaponMods.getName());
        }
        if(cpRedCustomWeaponMods.getPrice()!=modToUpdate.getPrice()){
            if (cpRedCustomWeaponMods.getPrice() !=-1) {
                if (cpRedCustomWeaponMods.getPrice() <= 0) {
                    throw new IllegalStateException("Cena modyfikacji broni nie może być mniejsza lub równa 0.");
                }
                modToUpdate.setPrice(cpRedCustomWeaponMods.getPrice());
            }
        }
        if(cpRedCustomWeaponMods.getSize()!=modToUpdate.getSize()){
            if (cpRedCustomWeaponMods.getSize() !=-1) {
                if (cpRedCustomWeaponMods.getSize() <= 0) {
                    throw new IllegalStateException("Rozmiar modyfikacji broni nie może być mniejszy lub równy 0.");
                }
                modToUpdate.setSize(cpRedCustomWeaponMods.getSize());
            }
        }
        if(cpRedCustomWeaponMods.getAvailability()!=null){
            modToUpdate.setAvailability(cpRedCustomWeaponMods.getAvailability());
        }
        if(cpRedCustomWeaponMods.getDescription()!=null){
            if (cpRedCustomWeaponMods.getDescription().isEmpty() ||
                    cpRedCustomWeaponMods.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis modyfikacji broni nie może być pusty.");
            }
            if (cpRedCustomWeaponMods.getDescription().length() > 500) {
                throw new IllegalStateException("Opis modyfikacji broni nie może być dłuższy niż 500 znaków.");
            }
            modToUpdate.setDescription(cpRedCustomWeaponMods.getDescription());
        }
        CpRedCustomWeaponMods updatedWeaponMod = cpRedCustomWeaponModsRepository.save(modToUpdate);
        return CustomReturnables.getOkResponseMap("Customowa modyfikacja broni została zaktualizowana.");
    }
//
//    // Pobierz wszystkie modyfikacje broni dla admina
//    public Map<String, Object> getAllWeaponModsForAdmin() {
//
//    }
}

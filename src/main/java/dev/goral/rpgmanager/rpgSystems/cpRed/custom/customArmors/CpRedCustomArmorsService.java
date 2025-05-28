package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.game.GameRepository;
import dev.goral.rpgmanager.game.GameStatus;
import dev.goral.rpgmanager.game.gameUsers.GameUsers;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRepository;
import dev.goral.rpgmanager.game.gameUsers.GameUsersRole;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedCustomArmorsService {
    private final CpRedCustomArmorsRepository cpRedCustomArmorsRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameUsersRepository gameUsersRepository;

    // Pobierz wszystkie customowe zbroje
    public Map<String, Object> getAllCustomArmors() {
        List<CpRedCustomArmorsDTO> allCustomArmorsList = cpRedCustomArmorsRepository.findAll().stream()
                .map(CpRedCustomArmors-> new CpRedCustomArmorsDTO(
                        CpRedCustomArmors.getGameId().getId(),
                        CpRedCustomArmors.getName(),
                        CpRedCustomArmors.getType().toString(),
                        CpRedCustomArmors.getArmorPoints(),
                        CpRedCustomArmors.getPenalty(),
                        CpRedCustomArmors.getPrice(),
                        CpRedCustomArmors.getAvailability().toString(),
                        CpRedCustomArmors.getDescription()
                ))
                .toList();
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Customowe pancerze zostały pobrane.");
        response.put("customArmors", allCustomArmorsList);
        return response;
    }
//
    // Pobierz customową zbroję po id
    public Map<String, Object> getCustomArmorById(Long armorId) {
        CpRedCustomArmorsDTO customArmor = cpRedCustomArmorsRepository.findById(armorId)
                .map(CpRedCustomArmors-> new CpRedCustomArmorsDTO(
                        CpRedCustomArmors.getGameId().getId(),
                        CpRedCustomArmors.getName(),
                        CpRedCustomArmors.getType().toString(),
                        CpRedCustomArmors.getArmorPoints(),
                        CpRedCustomArmors.getPenalty(),
                        CpRedCustomArmors.getPrice(),
                        CpRedCustomArmors.getAvailability().toString(),
                        CpRedCustomArmors.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Customowy pancerz o id " + armorId + " nie istnieje."));
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Customowa zbroja została pobrana.");
        response.put("customArmor", customArmor);
        return response;
    }
    public Map<String, Object> getCustomArmorByGame(Long gameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + gameId + " nie istnieje."));

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie jesteś graczem wybranej gry."));
        List<CpRedCustomArmorsDTO> gameCustomArmorsList = cpRedCustomArmorsRepository.findAllByGameId(game).stream()
                .map(CpRedCustomArmors-> new CpRedCustomArmorsDTO(
                        CpRedCustomArmors.getGameId().getId(),
                        CpRedCustomArmors.getName(),
                        CpRedCustomArmors.getType().toString(),
                        CpRedCustomArmors.getArmorPoints(),
                        CpRedCustomArmors.getPenalty(),
                        CpRedCustomArmors.getPrice(),
                        CpRedCustomArmors.getAvailability().toString(),
                        CpRedCustomArmors.getDescription()
                )).toList();

        Map<String,Object> response = CustomReturnables.getOkResponseMap("Customowe zbroje do gry zostały pobrane.");
        response.put("customArmor", gameCustomArmorsList);
        return response;
    }


    public Map<String, Object> addCustomArmor(CpRedCustomArmorsRequest cpRedCustomArmors) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        if (cpRedCustomArmors.getGameId() == null ||
                cpRedCustomArmors.getName() == null ||
                cpRedCustomArmors.getType() == null ||
                cpRedCustomArmors.getArmorPoints() < 0 ||
                cpRedCustomArmors.getPenalty() < 0 ||
                cpRedCustomArmors.getPrice() < 0 ||
                cpRedCustomArmors.getAvailability() == null||
                cpRedCustomArmors.getDescription() == null ) {
            throw new IllegalStateException("Wszystkie pola muszą być wypełnione.");
        }

        Game game = gameRepository.findById(cpRedCustomArmors.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + cpRedCustomArmors.getGameId() + " nie istnieje."));


        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + cpRedCustomArmors.getGameId() + " nie jest aktywna.");
        }

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), cpRedCustomArmors.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));

        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może dodać pancerz do gry.");
        }

        if (cpRedCustomArmorsRepository.existsByNameAndGameId(cpRedCustomArmors.getName(), game)) {
            throw new IllegalStateException("Customowy pancerz o tej nazwie już istnieje w tej grze.");
        }
        if (cpRedCustomArmors.getName().isEmpty() ||
                cpRedCustomArmors.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa pancerza nie może być pusta.");
        }
        if (cpRedCustomArmors.getName().length() > 255) {
            throw new IllegalStateException("Nazwa pancerza nie może być dłuższa niż 255 znaków.");
        }
        if (cpRedCustomArmors.getArmorPoints() < 0) {
            throw new IllegalStateException("Punkty pancerza nie może być mniejsza od 0.");
        }
        if (cpRedCustomArmors.getPenalty() < 0) {
            throw new IllegalStateException("Kara pancerza nie może być mniejsza od 0.");
        }
        if (cpRedCustomArmors.getPrice() < 0) {
            throw new IllegalStateException("Cena pancerza nie może być mniejsza lub równa 0.");
        }
        if (cpRedCustomArmors.getDescription().isEmpty() ||
                cpRedCustomArmors.getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Opis pancerza nie może być pusty.");
        }
        if (cpRedCustomArmors.getDescription().length() > 500) {
            throw new IllegalStateException("Opis pancerza nie może być dłuższy niż 500 znaków.");
        }

        CpRedCustomArmors newCustomArmor = new CpRedCustomArmors(
                null,
                game,
                cpRedCustomArmors.getName(),
                cpRedCustomArmors.getType(),
                cpRedCustomArmors.getArmorPoints(),
                cpRedCustomArmors.getPenalty(),
                cpRedCustomArmors.getPrice(),
                cpRedCustomArmors.getAvailability(),
                cpRedCustomArmors.getDescription()
        );

        cpRedCustomArmorsRepository.save(newCustomArmor);
        return CustomReturnables.getOkResponseMap("Customowy pancerz został dodany.");


    }

    // Modyfikuj customową zbroję
    public Map<String, Object> updateCustomArmor(Long armorId, CpRedCustomArmorsRequest cpRedCustomArmors) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        CpRedCustomArmors armorToUpdate = cpRedCustomArmorsRepository.findById(armorId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowy pancerz o id " + armorId + " nie istnieje"));

        Game game = gameRepository.findById(armorToUpdate.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + armorToUpdate.getGameId().getId() + " nie istnieje."));

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + armorToUpdate.getGameId().getId() + " nie jest aktywna.");
        }

        if (armorToUpdate.getGameId().getId() != cpRedCustomArmors.getGameId()) {
            throw new IllegalStateException("Nie można zmienić gry dla pancerza.");
        }

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), armorToUpdate.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));

        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może modyfikować pancerz.");
        }

        if(cpRedCustomArmors.getName() != null) {
            if (cpRedCustomArmorsRepository.existsByNameAndGameId(cpRedCustomArmors.getName(), armorToUpdate.getGameId())) {
                throw new IllegalStateException("Customowy pancerz o tej nazwie już istnieje.");
            }
            if (cpRedCustomArmors.getName().isEmpty() || cpRedCustomArmors.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa pancerza nie może być pusta.");
            }
            if (cpRedCustomArmors.getName().length() > 255) {
                throw new IllegalStateException("Nazwa pancerza jest za długa. Maksymalna długość to 255 znaków.");
            }
            armorToUpdate.setName(cpRedCustomArmors.getName());
        }

        if (cpRedCustomArmors.getType() != null) {
            armorToUpdate.setType(cpRedCustomArmors.getType());
        }

        if (cpRedCustomArmors.getArmorPoints() != armorToUpdate.getArmorPoints()) {
            if (cpRedCustomArmors.getArmorPoints() != -1){
                if (cpRedCustomArmors.getArmorPoints() < 0) {
                    throw new IllegalStateException("Punkty pancerza muszą być większe lub równe 0.");
                }
                armorToUpdate.setArmorPoints(cpRedCustomArmors.getArmorPoints());
            }
        }

        if (cpRedCustomArmors.getPenalty() != armorToUpdate.getPenalty()) {
            if (cpRedCustomArmors.getPenalty() != -1){
                if (cpRedCustomArmors.getPenalty() < 0) {
                    throw new IllegalStateException("Kara pancerza musi być większa lub równa 0.");
                }
                armorToUpdate.setPenalty(cpRedCustomArmors.getPenalty());
            }
        }

        if (cpRedCustomArmors.getPrice() != armorToUpdate.getPrice()) {
            if (cpRedCustomArmors.getPrice() != -1){
                if (cpRedCustomArmors.getPrice() <= 0) {
                    throw new IllegalStateException("Cena pancerza musi być większa od 0.");
                }
                armorToUpdate.setPrice(cpRedCustomArmors.getPrice());
            }
        }

        if (cpRedCustomArmors.getAvailability() != null) {
            armorToUpdate.setAvailability(cpRedCustomArmors.getAvailability());
        }

        if (cpRedCustomArmors.getDescription() != null) {
            if (cpRedCustomArmors.getDescription().length() > 500) {
                throw new IllegalStateException("Opis pancerza jest za długi. Maksymalna długość to 500 znaków.");
            }
            if (cpRedCustomArmors.getDescription().isEmpty() || cpRedCustomArmors.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis pancerza nie może być pusty.");
            }
            armorToUpdate.setDescription(cpRedCustomArmors.getDescription());
        }


        cpRedCustomArmorsRepository.save(armorToUpdate);

        return CustomReturnables.getOkResponseMap("Customowy pancerz został zmodyfikowany.");
    }

    public Map<String, Object> getAllCustomArmorsForAdmin() {
        List<CpRedCustomArmors> allCustomArmorsList = cpRedCustomArmorsRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe pancerze zostały pobrane dla administratora.");
        response.put("customArmors", allCustomArmorsList);
        return response;
    }
}

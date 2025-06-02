package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customEquipments;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
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
public class CpRedCustomEquipmentsService {
    private final CpRedCustomEquipmentsRepository cpRedCustomEquipmentsRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final UserRepository userRepository;

    public Map<String, Object> getAllEquipments(){
        List<CpRedCustomEquipmentsDTO> allCustomEquipments = cpRedCustomEquipmentsRepository.findAll().stream()
                .map(CpRedCustomEquipments -> new CpRedCustomEquipmentsDTO(
                        CpRedCustomEquipments.getGameId().getId(),
                        CpRedCustomEquipments.getName(),
                        CpRedCustomEquipments.getPrice(),
                        CpRedCustomEquipments.getAvailability().toString(),
                        CpRedCustomEquipments.getDescription()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe wyposażenie  zostały pobrane.");
        response.put("customEquipments", allCustomEquipments);
        return response;
    }

    public Map<String, Object> getEquipmentById(Long equipmentId){
        CpRedCustomEquipmentsDTO cpRedCustomEquipments = cpRedCustomEquipmentsRepository.findById(equipmentId)
                .map(CpRedCustomEquipments -> new CpRedCustomEquipmentsDTO(
                        CpRedCustomEquipments.getGameId().getId(),
                        CpRedCustomEquipments.getName(),
                        CpRedCustomEquipments.getPrice(),
                        CpRedCustomEquipments.getAvailability().toString(),
                        CpRedCustomEquipments.getDescription()
                )).orElseThrow(()-> new ResourceNotFoundException("Customowe wyposażenie o id " + equipmentId + " nie istnieje."));
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Customowe wyposażenie zostały pobrane.");
        response.put("customEquipments", cpRedCustomEquipments);
        return response;
    }

    public Map<String, Object> getEquipmentsByGame(Long gameId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + gameId + " nie istnieje."));

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie jesteś graczem wybranej gry."));
        List<CpRedCustomEquipmentsDTO> allCustomEquipments = cpRedCustomEquipmentsRepository.findAllByGameId(game).stream()
                .map(CpRedCustomEquipments -> new CpRedCustomEquipmentsDTO(
                        CpRedCustomEquipments.getGameId().getId(),
                        CpRedCustomEquipments.getName(),
                        CpRedCustomEquipments.getPrice(),
                        CpRedCustomEquipments.getAvailability().toString(),
                        CpRedCustomEquipments.getDescription()
                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe wyposażenie do gry zostało pobrane.");
        response.put("customEquipments", allCustomEquipments);
        return response;
    }

    public Map<String, Object> addEquipment(CpRedCustomEquipmentsRequest cpRedCustomEquipments){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if(cpRedCustomEquipments.getGameId()==null||
                cpRedCustomEquipments.getName()==null||
                cpRedCustomEquipments.getPrice()<0||
                cpRedCustomEquipments.getAvailability()==null||
                cpRedCustomEquipments.getDescription()==null){
            throw new IllegalStateException("Wszystkie pola muszą być wypełnione.");
        }
        Game game = gameRepository.findById(cpRedCustomEquipments.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id " + cpRedCustomEquipments.getGameId() + " nie istnieje."));
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), cpRedCustomEquipments.getGameId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));

        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może dodać wyposażenie do gry.");
        }
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + cpRedCustomEquipments.getGameId() + " nie jest aktywna.");
        }
        if (cpRedCustomEquipmentsRepository.existsByNameAndGameId(cpRedCustomEquipments.getName(), game)) {
            throw new IllegalStateException("Customowe wyposażenie o tej nazwie już istnieje.");
        }
        if (cpRedCustomEquipments.getName().isEmpty() ||
                cpRedCustomEquipments.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa wyposażenia nie może być pusta.");
        }
        if (cpRedCustomEquipments.getName().length() > 255) {
            throw new IllegalStateException("Nazwa wyposażenia nie może być dłuższa niż 255 znaków.");
        }
        if (cpRedCustomEquipments.getPrice() < 0) {
            throw new IllegalStateException("Cena wyposażenia nie może być mniejsza lub równa 0.");
        }
        if (cpRedCustomEquipments.getDescription().isEmpty() ||
                cpRedCustomEquipments.getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Opis wyposażenia nie może być pusty.");
        }
        if (cpRedCustomEquipments.getDescription().length() > 500) {
            throw new IllegalStateException("Opis wyposażenia nie może być dłuższy niż 500 znaków.");
        }
        CpRedCustomEquipments newCpRedCustomEquipments = new CpRedCustomEquipments(
                null,
                game,
                cpRedCustomEquipments.getName(),
                cpRedCustomEquipments.getPrice(),
                cpRedCustomEquipments.getAvailability(),
                cpRedCustomEquipments.getDescription()
        );
        cpRedCustomEquipmentsRepository.save(newCpRedCustomEquipments);
        return CustomReturnables.getOkResponseMap("Customowe wyposażenie zostało dodane.");
    }

    public Map<String, Object> updateEquipment(Long equipmentId, CpRedCustomEquipmentsRequest cpRedCustomEquipments){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        CpRedCustomEquipments equipmentToUpdate = cpRedCustomEquipmentsRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowe wyposażenie o id " + equipmentId + " nie istnieje."));

        Game game = gameRepository.findById(equipmentToUpdate.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra o id "+equipmentToUpdate.getGameId().getId()+" nie istnieje."));
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra o id " + equipmentToUpdate.getGameId().getId() + " nie jest aktywna.");
        }

        if (equipmentToUpdate.getGameId().getId() != cpRedCustomEquipments.getGameId()) {
            throw new IllegalStateException("Nie można zmienić gry dla wyposażenia.");
        }

        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), equipmentToUpdate.getGameId().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do podanej gry."));
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER) {
            throw new IllegalStateException("Tylko GM może modyfikować wyposażenie.");
        }



        if (cpRedCustomEquipments.getName() != null) {
            if (cpRedCustomEquipmentsRepository.existsByNameAndGameId(cpRedCustomEquipments.getName(), game)) {
                throw new IllegalStateException("Customowy wyposażenie o tej nazwie już istnieje.");
            }
            if (cpRedCustomEquipments.getName().isEmpty() ||
                    cpRedCustomEquipments.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa wyposażenia nie może być pusta.");
            }
            if (cpRedCustomEquipments.getName().length() > 255) {
                throw new IllegalStateException("Nazwa wyposażenia nie może być dłuższa niż 255 znaków.");
            }
            equipmentToUpdate.setName(cpRedCustomEquipments.getName());
        }
        if(cpRedCustomEquipments.getPrice()!=equipmentToUpdate.getPrice()){
            if(cpRedCustomEquipments.getPrice()!=-1) {
                if (cpRedCustomEquipments.getPrice() <= 0) {
                    throw new IllegalStateException("Cena wyposażenia nie może być mniejsza lub równa 0.");
                }
                equipmentToUpdate.setPrice(cpRedCustomEquipments.getPrice());
            }
        }
        if(cpRedCustomEquipments.getAvailability()!=null){
            equipmentToUpdate.setAvailability(cpRedCustomEquipments.getAvailability());
        }
        if(cpRedCustomEquipments.getDescription()!=null){
            if (cpRedCustomEquipments.getDescription().isEmpty() ||
                    cpRedCustomEquipments.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis wyposażenia nie może być pusty.");
            }
            if (cpRedCustomEquipments.getDescription().length() > 500) {
                throw new IllegalStateException("Opis wyposażenia nie może być dłuższy niż 500 znaków.");
            }
            equipmentToUpdate.setDescription(cpRedCustomEquipments.getDescription());
        }
        cpRedCustomEquipmentsRepository.save(equipmentToUpdate);
        return CustomReturnables.getOkResponseMap("Customowe wyposażenie zostało zmodyfikowane.");
    }

    public Map<String, Object> getAllEquipmentsForAdmin(){
        List<CpRedCustomEquipments> allEquipments = cpRedCustomEquipmentsRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe wyposażenie zostało pobrane.");
        response.put("customEquipments", allEquipments);
        return response;
    }
}

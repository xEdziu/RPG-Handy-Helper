package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEquipment;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor.CpRedCharacterArmor;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.equipments.CpRedEquipments;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.equipments.CpRedEquipmentsRepository;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CpRedCharacterEquipmentService {
    private final CpRedCharacterEquipmentRepository cpRedCharacterEquipmentRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedEquipmentsRepository cpRedEquipmentsRepository;


    public Map<String, Object> getCharacterEquipment(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterEquipmentDTO> characterEquipments = cpRedCharacterEquipmentRepository.findAllByCharacter(character)
                .stream()
                .map(equipment -> new CpRedCharacterEquipmentDTO(
                        equipment.getId(),
                        equipment.getCharacter().getId(),
                        equipment.getItem().getId(),
                        equipment.getQuantity(),
                        equipment.getStatus().toString(),
                        equipment.getDescription()))
                .toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wyposażenie postaci pobrane pomyślnie");
        response.put("characterEquipments", characterEquipments);
        return response;
    }

    public Map<String, Object> createCharacterEquipment(AddCharacterEquipmentRequest addCharacterEquipmentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy podano wszystkie wymagane pola w request
        if (addCharacterEquipmentRequest.getCharacterId() == null ||
            addCharacterEquipmentRequest.getItemId() == null ||
            addCharacterEquipmentRequest.getQuantity() == null ||
            addCharacterEquipmentRequest.getStatus() == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterEquipmentRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje item o podanym ID
        CpRedEquipments item = cpRedEquipmentsRepository.findById(addCharacterEquipmentRequest.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Przedmiot o podanym ID nie został znaleziony."));

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
                throw new ResourceNotFoundException("Tylko GM może dodawać wyposażenie postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Sprawdzenie, czy postać ma już to wyposażenie
        if (cpRedCharacterEquipmentRepository.existsByCharacterAndItem(character, item)) {
            throw new IllegalArgumentException("Postać ma już to wyposażenie.");
        }

        if (addCharacterEquipmentRequest.getQuantity() < 0) {
            throw new IllegalArgumentException("Ilość wyposażenia musi być większa niż 0.");
        }

        // Tworzenie nowego wyposażenia postaci
        CpRedCharacterEquipment newCharacterEquipment = new CpRedCharacterEquipment(
                null,
                item,
                character,
                addCharacterEquipmentRequest.getQuantity(),
                addCharacterEquipmentRequest.getStatus(),
                item.getDescription()
        );

        cpRedCharacterEquipmentRepository.save(newCharacterEquipment);

        return CustomReturnables.getOkResponseMap("Wyposażenie zostało dodane do postaci");
    }

    public Map<String, Object> updateCharacterEquipment(Long characterEquipmentId,
                                                        UpdateCharacterEquipmentRequest updateCharacterEquipmentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje wyposażenie postaci o podanym ID
        CpRedCharacterEquipment characterEquipmentToUpdate = cpRedCharacterEquipmentRepository.findById(characterEquipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Wyposażenie postaci o podanym ID nie zostało znalezione."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterEquipmentToUpdate.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje item o podanym ID
        CpRedEquipments item = cpRedEquipmentsRepository.findById(characterEquipmentToUpdate.getItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Przedmiot o podanym ID nie został znaleziony."));

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
                throw new ResourceNotFoundException("Tylko GM może modyfikować wyposażenie postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Sprawdzenie ilości przedmiotów
        if (updateCharacterEquipmentRequest.getQuantity() != null) {
            if (updateCharacterEquipmentRequest.getQuantity() < 0) {
                throw new IllegalArgumentException("Ilość wyposażenia musi być większa lub równa 0.");
            }
            characterEquipmentToUpdate.setQuantity(updateCharacterEquipmentRequest.getQuantity());
        }

        // Sprawdzenie statusu przedmiotów
        if (updateCharacterEquipmentRequest.getStatus() != null) {
            characterEquipmentToUpdate.setStatus(updateCharacterEquipmentRequest.getStatus());
        }

        // Sprawdzenie opisu przedmiotów
        if (updateCharacterEquipmentRequest.getDescription() != null) {
            if (updateCharacterEquipmentRequest.getDescription().length() > 1000) {
                throw new IllegalArgumentException("Opis nie może być dłuższy niż 1000 znaków.");
            }
            characterEquipmentToUpdate.setDescription(updateCharacterEquipmentRequest.getDescription());
        }

        // Zapisanie zmodyfikowanego wyposażenia postaci
        cpRedCharacterEquipmentRepository.save(characterEquipmentToUpdate);

        return CustomReturnables.getOkResponseMap("Wyposażenie postaci zostało pomyślnie zmodyfikowane");
    }

    public Map<String, Object> deleteCharacterEquipment(Long characterEquipmentId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje wyposażenie postaci o podanym ID
        CpRedCharacterEquipment characterEquipmentToDelete = cpRedCharacterEquipmentRepository.findById(characterEquipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Wyposażenie postaci o podanym ID nie zostało znalezione."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterEquipmentToDelete.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje item o podanym ID
        CpRedEquipments item = cpRedEquipmentsRepository.findById(characterEquipmentToDelete.getItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Przedmiot o podanym ID nie został znaleziony."));

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
                throw new ResourceNotFoundException("Tylko GM może modyfikować wyposażenie postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Usunięcie wyposażenia postaci
        cpRedCharacterEquipmentRepository.delete(characterEquipmentToDelete);

        return CustomReturnables.getOkResponseMap("Wyposażenie postaci zostało pomyślnie usunięte");
    }

    public Map<String, Object> getAllCharacterEquipments() {
        List<CpRedCharacterEquipment> allCharacterEquipments = cpRedCharacterEquipmentRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie wyposażenia wszystkich postaci pobrane pomyślnie");
        response.put("characterEquipments", allCharacterEquipments);
        return response;
    }
}

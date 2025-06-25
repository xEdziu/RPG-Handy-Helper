package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomEquipment;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customEquipments.CpRedCustomEquipments;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customEquipments.CpRedCustomEquipmentsRepository;
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
public class CpRedCharacterCustomEquipmentService {
    private final CpRedCharacterCustomEquipmentRepository cpRedCharacterCustomEquipmentRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedCustomEquipmentsRepository cpRedCustomEquipmentsRepository;

    public Map<String, Object> getCharacterCustomEquipment(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterCustomEquipmentDTO> characterCustomEquipments = cpRedCharacterCustomEquipmentRepository.findAllByCharacter(character)
                .stream()
                .map(equipment -> new CpRedCharacterCustomEquipmentDTO(
                        equipment.getId(),
                        equipment.getCharacter().getId(),
                        equipment.getCustomItem().getId(),
                        equipment.getQuantity(),
                        equipment.getStatus().toString(),
                        equipment.getDescription()))
                .toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe wyposażenie postaci pobrane pomyślnie");
        response.put("characterCustomEquipments", characterCustomEquipments);
        return response;

    }

    public Map<String, Object> createCharacterCustomEquipment(AddCharacterCustomEquipmentRequest addCharacterCustomEquipmentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy podano wszystkie wymagane pola w request
        if (addCharacterCustomEquipmentRequest.getCharacterId() == null ||
                addCharacterCustomEquipmentRequest.getCustomItemId() == null ||
                addCharacterCustomEquipmentRequest.getQuantity() == null ||
                addCharacterCustomEquipmentRequest.getStatus() == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterCustomEquipmentRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje item o podanym ID
        CpRedCustomEquipments customItem = cpRedCustomEquipmentsRepository.findById(addCharacterCustomEquipmentRequest.getCustomItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Customowy przedmiot o podanym ID nie został znaleziony."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        // Czy użytkownik należy do gry, do której należy postać
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        // Czy postać należy do tej samej gry co przedmiot
        if (!Objects.equals(character.getGame().getId(), customItem.getGameId().getId())) {
            throw new ResourceNotFoundException("Postać nie należy do gry powiązanej z podanym customowym przedmiotem.");
        }

        // Czy ktoś chce zmieniać swoją postać lub jest GM-em w tej grze
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może dodawać customowe wyposażenie postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Sprawdzenie, czy postać ma już to wyposażenie
        if (cpRedCharacterCustomEquipmentRepository.existsByCharacterAndCustomItem(character, customItem)) {
            throw new IllegalArgumentException("Postać ma już to customowe wyposażenie.");
        }

        if (addCharacterCustomEquipmentRequest.getQuantity() < 0) {
            throw new IllegalArgumentException("Ilość customowego wyposażenia musi być większa niż 0.");
        }

        // Tworzenie nowego wyposażenia postaci
        CpRedCharacterCustomEquipment newCharacterCustomEquipment = new CpRedCharacterCustomEquipment(
                null,
                customItem,
                character,
                addCharacterCustomEquipmentRequest.getQuantity(),
                addCharacterCustomEquipmentRequest.getStatus(),
                customItem.getDescription()
        );

        cpRedCharacterCustomEquipmentRepository.save(newCharacterCustomEquipment);

        return CustomReturnables.getOkResponseMap("Customowe wyposażenie zostało dodane do postaci");
    }

    public Map<String, Object> updateCharacterCustomEquipment(Long characterCustomEquipmentId,
                                                              UpdateCharacterCustomEquipmentRequest updateCharacterCustomEquipmentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje wyposażenie postaci o podanym ID
        CpRedCharacterCustomEquipment characterCustomEquipmentToUpdate = cpRedCharacterCustomEquipmentRepository.findById(characterCustomEquipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowe wyposażenie postaci o podanym ID nie zostało znalezione."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterCustomEquipmentToUpdate.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje item o podanym ID
        CpRedCustomEquipments customItem = cpRedCustomEquipmentsRepository.findById(characterCustomEquipmentToUpdate.getCustomItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customowy przedmiot o podanym ID nie został znaleziony."));

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
                throw new ResourceNotFoundException("Tylko GM może modyfikować customowe wyposażenie postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Sprawdzenie ilości przedmiotów
        if (updateCharacterCustomEquipmentRequest.getQuantity() != null) {
            if (updateCharacterCustomEquipmentRequest.getQuantity() < 0) {
                throw new IllegalArgumentException("Ilość customowego wyposażenia musi być większa lub równa 0.");
            }
            characterCustomEquipmentToUpdate.setQuantity(updateCharacterCustomEquipmentRequest.getQuantity());
        }

        // Sprawdzenie statusu przedmiotów
        if (updateCharacterCustomEquipmentRequest.getStatus() != null) {
            characterCustomEquipmentToUpdate.setStatus(updateCharacterCustomEquipmentRequest.getStatus());
        }

        // Sprawdzenie opisu przedmiotów
        if (updateCharacterCustomEquipmentRequest.getDescription() != null) {
            if (updateCharacterCustomEquipmentRequest.getDescription().length() > 1000) {
                throw new IllegalArgumentException("Opis nie może być dłuższy niż 1000 znaków.");
            }
            characterCustomEquipmentToUpdate.setDescription(updateCharacterCustomEquipmentRequest.getDescription());
        }

        // Zapisanie zmodyfikowanego wyposażenia postaci
        cpRedCharacterCustomEquipmentRepository.save(characterCustomEquipmentToUpdate);

        return CustomReturnables.getOkResponseMap("Customowe wyposażenie postaci zostało pomyślnie zmodyfikowane");
    }

    public Map<String, Object> deleteCharacterCustomEquipment(Long characterCustomEquipmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje wyposażenie postaci o podanym ID
        CpRedCharacterCustomEquipment characterCustomEquipmentToDelete = cpRedCharacterCustomEquipmentRepository.findById(characterCustomEquipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Customowe wyposażenie postaci o podanym ID nie zostało znalezione."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterCustomEquipmentToDelete.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje item o podanym ID
        CpRedCustomEquipments customItem = cpRedCustomEquipmentsRepository.findById(characterCustomEquipmentToDelete.getCustomItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customowy przedmiot o podanym ID nie został znaleziony."));

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
                throw new ResourceNotFoundException("Tylko GM może modyfikować customowe wyposażenie postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Usunięcie wyposażenia postaci
        cpRedCharacterCustomEquipmentRepository.delete(characterCustomEquipmentToDelete);

        return CustomReturnables.getOkResponseMap("Customowe wyposażenie postaci zostało pomyślnie usunięte");
    }

    public Map<String, Object> getAllCharacterCustomEquipments() {
        List<CpRedCharacterCustomEquipment> allCharacterCustomEquipments = cpRedCharacterCustomEquipmentRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie customowe wyposażenia wszystkich postaci pobrane pomyślnie");
        response.put("characterCustomEquipments", allCharacterCustomEquipments);
        return response;
    }
}

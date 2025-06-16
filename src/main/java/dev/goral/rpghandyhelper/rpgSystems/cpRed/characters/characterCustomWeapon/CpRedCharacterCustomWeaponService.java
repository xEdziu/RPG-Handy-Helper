package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeapons.CpRedCustomWeapons;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeapons.CpRedCustomWeaponsRepository;
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
public class CpRedCharacterCustomWeaponService {
    private final CpRedCharacterCustomWeaponRepository cpRedCharacterCustomWeaponRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedCustomWeaponsRepository cpRedCustomWeaponsRepository;

    public Map<String, Object> getCharacterCustomWeapons(Long characterId){
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterCustomWeaponDTO> characterCustomWeapons = cpRedCharacterCustomWeaponRepository.findAllByCharacter(character)
                .stream()
                .map(weapons -> new CpRedCharacterCustomWeaponDTO(
                                weapons.getId(),
                                weapons.getBaseCustomWeapon().getId(),
                                weapons.getCharacter().getId(),
                                weapons.getDmg(),
                                weapons.getMagazineCapacity(),
                                weapons.getNumberOfAttacks(),
                                weapons.getHandType(),
                                weapons.getIsHidden(),
                                weapons.getQuality().toString(),
                                weapons.getStatus().toString(),
                                weapons.getDescription()
                        )
                )
                .toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Customowe bronie postaci pobrane pomyślnie");
        response.put("characterCustomWeapons", characterCustomWeapons);
        return response;
    }

    public Map<String, Object> createCharacterCustomWeapon(AddCharacterCustomWeaponRequest addCharacterCustomWeaponRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy podano wszystkie wymagane pola w request
        if (addCharacterCustomWeaponRequest.getBaseCustomWeaponId() == null ||
                addCharacterCustomWeaponRequest.getCharacterId() == null ||
                addCharacterCustomWeaponRequest.getStatus() == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterCustomWeaponRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje broń o podanym ID
        CpRedCustomWeapons weapon = cpRedCustomWeaponsRepository.findById(addCharacterCustomWeaponRequest.getBaseCustomWeaponId())
                .orElseThrow(() -> new ResourceNotFoundException("Broń o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        // Czy użytkownik należy do gry, do której należy postać
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        // Czy postać należy do gry, do której jest przypisana customowa broń
        if (!Objects.equals(character.getGame().getId(), weapon.getGameId().getId())) {
            throw new ResourceNotFoundException("Postać nie należy do gry, do której jest przypisana customowa broń.");
        }

        // Czy ktoś chce zmieniać swoją postać lub jest GM-em w tej grze
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może dodawać customowe bronie postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Tworzenie nowej klasy postaci
        CpRedCharacterCustomWeapon newCharacterCustomWeapon = new CpRedCharacterCustomWeapon(
                null,
                weapon,
                character,
                weapon.getDamage(),
                weapon.getMagazineCapacity(),
                weapon.getNumberOfAttacks(),
                weapon.getHandType(),
                weapon.getIsHidden(),
                weapon.getQuality(),
                addCharacterCustomWeaponRequest.getStatus(),
                weapon.getDescription()
        );

        cpRedCharacterCustomWeaponRepository.save(newCharacterCustomWeapon);

        return CustomReturnables.getOkResponseMap("Customowa broń została dodana do postaci");
    }

    public Map<String, Object> updateCharacterCustomWeapon(Long characterCustomWeaponId,
                                                           UpdateCharacterCustomWeaponRequest updateCharacterCustomWeaponRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje broń postaci o podanym ID
        CpRedCharacterCustomWeapon characterCustomWeaponToUpdate = cpRedCharacterCustomWeaponRepository.findById(characterCustomWeaponId)
                .orElseThrow(() -> new ResourceNotFoundException("Broń postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterCustomWeaponToUpdate.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje broń o podanym ID
        CpRedCustomWeapons weapon = cpRedCustomWeaponsRepository.findById(characterCustomWeaponToUpdate.getBaseCustomWeapon().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Broń o podanym ID nie została znaleziona."));

        Game game = gameRepository.findById(character.getGame().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gra powiązana z postacią nie została znaleziona."));

        // Czy użytkownik należy do gry, do której należy postać
        GameUsers gameUsers = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nie należysz do gry powiązanej z podaną postacią."));

        // Czy postać należy do gry, do której jest przypisana customowa broń
        if (!Objects.equals(character.getGame().getId(), weapon.getGameId().getId())) {
            throw new ResourceNotFoundException("Postać nie należy do gry, do której jest przypisana customowa broń.");
        }

        // Czy ktoś chce zmieniać swoją postać lub jest GM-em w tej grze
        if (gameUsers.getRole() != GameUsersRole.GAMEMASTER){
            if (character.getType() != CpRedCharactersType.NPC) {
                if (!Objects.equals(currentUser.getId(), character.getUser().getId())) {
                    throw new ResourceNotFoundException("Zalogowany użytkownik nie jest GM-em w tej grze ani nie jest właścicielem postaci.");
                }
            } else {
                throw new ResourceNotFoundException("Tylko GM może zmieniać customowe bronie postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Sprawdzenie dmg
        if (updateCharacterCustomWeaponRequest.getDmg() != null) {
            if (updateCharacterCustomWeaponRequest.getDmg() < 0) {
                throw new IllegalArgumentException("Wartość obrażeń nie może być mniejsza niż 0.");
            }
            characterCustomWeaponToUpdate.setDmg(updateCharacterCustomWeaponRequest.getDmg());
        }

        // Sprawdzenie magazineCapacity
        if (updateCharacterCustomWeaponRequest.getMagazineCapacity() != null) {
            if (updateCharacterCustomWeaponRequest.getMagazineCapacity() < 0) {
                throw new IllegalArgumentException("Pojemność magazynka nie może być mniejsza niż 0.");
            }
            characterCustomWeaponToUpdate.setMagazineCapacity(updateCharacterCustomWeaponRequest.getMagazineCapacity());
        }

        // Sprawdzenie numberOfAttacks
        if (updateCharacterCustomWeaponRequest.getNumberOfAttacks() != null) {
            if (updateCharacterCustomWeaponRequest.getNumberOfAttacks() < 0) {
                throw new IllegalArgumentException("Liczba ataków nie może być mniejsza niż 0.");
            }
            characterCustomWeaponToUpdate.setNumberOfAttacks(updateCharacterCustomWeaponRequest.getNumberOfAttacks());
        }

        // Sprawdzenie handType
        if (updateCharacterCustomWeaponRequest.getHandType() != null) {
            if (updateCharacterCustomWeaponRequest.getHandType() < 0) {
                throw new IllegalArgumentException("Typ ręki nie może być mniejszy niż 0.");
            }
            characterCustomWeaponToUpdate.setHandType(updateCharacterCustomWeaponRequest.getHandType());
        }

        // Sprawdzenie isHidden
        if (updateCharacterCustomWeaponRequest.getIsHidden() != null) {
            characterCustomWeaponToUpdate.setIsHidden(updateCharacterCustomWeaponRequest.getIsHidden());
        }

        // Sprawdzenie quality
        if (updateCharacterCustomWeaponRequest.getQuality() != null) {
            characterCustomWeaponToUpdate.setQuality(updateCharacterCustomWeaponRequest.getQuality());
        }

        // Sprawdzenie status
        if (updateCharacterCustomWeaponRequest.getStatus() != null) {
            characterCustomWeaponToUpdate.setStatus(updateCharacterCustomWeaponRequest.getStatus());
        }

        // Sprawdzenie opisu
        if (updateCharacterCustomWeaponRequest.getDescription() != null) {
            if (updateCharacterCustomWeaponRequest.getDescription().length() > 500) {
                throw new IllegalArgumentException("Opis nie może być dłuższy niż 500 znaków.");
            }
            characterCustomWeaponToUpdate.setDescription(updateCharacterCustomWeaponRequest.getDescription());
        }

        // Zapisanie zmodyfikowanej broni postaci
        cpRedCharacterCustomWeaponRepository.save(characterCustomWeaponToUpdate);
        
        return CustomReturnables.getOkResponseMap("Customowa broń postaci została pomyślnie zmodyfikowana");
    }

    public Map<String, Object> deleteCharacterCustomWeapon(Long characterCustomWeaponId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje broń postaci o podanym ID
        CpRedCharacterCustomWeapon characterCustomWeapon = cpRedCharacterCustomWeaponRepository.findById(characterCustomWeaponId)
                .orElseThrow(() -> new ResourceNotFoundException("Broń postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterCustomWeapon.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje broń o podanym ID
        CpRedCustomWeapons weapon = cpRedCustomWeaponsRepository.findById(characterCustomWeapon.getBaseCustomWeapon().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Broń o podanym ID nie została znaleziona."));

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
                throw new ResourceNotFoundException("Tylko GM może usuwać customowe bronie postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        cpRedCharacterCustomWeaponRepository.deleteById(characterCustomWeaponId);
        
        return CustomReturnables.getOkResponseMap("Customowa broń postaci została pomyślnie usunięta");
    }

    public Map<String, Object> getAllCharacterCustomWeapons(){
        List<CpRedCharacterCustomWeapon> allCharacterCustomWeapons = cpRedCharacterCustomWeaponRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie customowe bronie postaci pobrane pomyślnie");
        response.put("allCharacterCustomWeapons", allCharacterCustomWeapons);
        return response;
    }
}

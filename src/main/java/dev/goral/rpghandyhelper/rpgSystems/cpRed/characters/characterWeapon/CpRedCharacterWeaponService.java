package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharactersType;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsQuality;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.stats.CpRedStats;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons.CpRedWeapons;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons.CpRedWeaponsRepository;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CpRedCharacterWeaponService {
    private final CpRedCharacterWeaponRepository cpRedCharacterWeaponRepository;
    private final UserRepository userRepository;
    private final CpRedCharactersRepository cpRedCharactersRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;
    private final CpRedWeaponsRepository cpRedWeaponsRepository;

    public Map<String, Object> getCharacterWeapons(Long characterId) {
        CpRedCharacters character = cpRedCharactersRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));
        List<CpRedCharacterWeaponDTO> characterWeapons = cpRedCharacterWeaponRepository.findAllByCharacter(character)
                .stream()
                .map(weapons -> new CpRedCharacterWeaponDTO(
                        weapons.getId(),
                        weapons.getBaseWeapon().getId(),
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
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Bronie postaci pobrane pomyślnie");
        response.put("characterWeapons", characterWeapons);
        return response;
    }

    public Map<String, Object> createCharacterWeapon(AddCharacterWeaponRequest addCharacterWeaponRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy podano wszystkie wymagane pola w request
        if (addCharacterWeaponRequest.getBaseWeaponId() == null ||
                addCharacterWeaponRequest.getCharacterId() == null ||
                addCharacterWeaponRequest.getStatus() == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(addCharacterWeaponRequest.getCharacterId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje broń o podanym ID
        CpRedWeapons weapon = cpRedWeaponsRepository.findById(addCharacterWeaponRequest.getBaseWeaponId())
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
                throw new ResourceNotFoundException("Tylko GM może dodawać bronie postaci NPC.");
            }
        }
        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Tworzenie nowej klasy postaci
        CpRedCharacterWeapon newCharacterWeapon = new CpRedCharacterWeapon(
                null,
                weapon,
                character,
                weapon.getDamage(),
                weapon.getMagazineCapacity(),
                weapon.getNumberOfAttacks(),
                weapon.getHandType(),
                weapon.isHidden(),
                weapon.getQuality(),
                addCharacterWeaponRequest.getStatus(),
                weapon.getDescription()
        );

        cpRedCharacterWeaponRepository.save(newCharacterWeapon);
        return CustomReturnables.getOkResponseMap("Broń została dodana do postaci");
    }

    public Map<String, Object> updateCharacterWeapon(Long characterWeaponId,
                                                     UpdateCharacterWeaponRequest updateCharacterWeaponRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje broń postaci o podanym ID
        CpRedCharacterWeapon characterWeaponToUpdate = cpRedCharacterWeaponRepository.findById(characterWeaponId)
                .orElseThrow(() -> new ResourceNotFoundException("Broń postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterWeaponToUpdate.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje broń o podanym ID
        CpRedWeapons weapon = cpRedWeaponsRepository.findById(characterWeaponToUpdate.getBaseWeapon().getId())
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
                throw new ResourceNotFoundException("Tylko GM może zmieniać bronie postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        // Sprawdzenie dmg
        if (updateCharacterWeaponRequest.getDmg() != null) {
            if (updateCharacterWeaponRequest.getDmg() < 0) {
                throw new IllegalArgumentException("Wartość obrażeń nie może być mniejsza niż 0.");
            }
            characterWeaponToUpdate.setDmg(updateCharacterWeaponRequest.getDmg());
        }

        // Sprawdzenie magazineCapacity
        if (updateCharacterWeaponRequest.getMagazineCapacity() != null) {
            if (updateCharacterWeaponRequest.getMagazineCapacity() < 0) {
                throw new IllegalArgumentException("Pojemność magazynka nie może być mniejsza niż 0.");
            }
            characterWeaponToUpdate.setMagazineCapacity(updateCharacterWeaponRequest.getMagazineCapacity());
        }

        // Sprawdzenie numberOfAttacks
        if (updateCharacterWeaponRequest.getNumberOfAttacks() != null) {
            if (updateCharacterWeaponRequest.getNumberOfAttacks() < 0) {
                throw new IllegalArgumentException("Liczba ataków nie może być mniejsza niż 0.");
            }
            characterWeaponToUpdate.setNumberOfAttacks(updateCharacterWeaponRequest.getNumberOfAttacks());
        }

        // Sprawdzenie handType
        if (updateCharacterWeaponRequest.getHandType() != null) {
            if (updateCharacterWeaponRequest.getHandType() < 0) {
                throw new IllegalArgumentException("Typ ręki nie może być mniejszy niż 0.");
            }
            characterWeaponToUpdate.setHandType(updateCharacterWeaponRequest.getHandType());
        }

        // Sprawdzenie isHidden
        if (updateCharacterWeaponRequest.getIsHidden() != null) {
            characterWeaponToUpdate.setIsHidden(updateCharacterWeaponRequest.getIsHidden());
        }

        // Sprawdzenie quality
        if (updateCharacterWeaponRequest.getQuality() != null) {
            characterWeaponToUpdate.setQuality(updateCharacterWeaponRequest.getQuality());
        }

        // Sprawdzenie status
        if (updateCharacterWeaponRequest.getStatus() != null) {
            characterWeaponToUpdate.setStatus(updateCharacterWeaponRequest.getStatus());
        }

        // Sprawdzenie opisu
        if (updateCharacterWeaponRequest.getDescription() != null) {
            if (updateCharacterWeaponRequest.getDescription().length() > 500) {
                throw new IllegalArgumentException("Opis nie może być dłuższy niż 500 znaków.");
            }
            characterWeaponToUpdate.setDescription(updateCharacterWeaponRequest.getDescription());
        }

        // Zapisanie zmodyfikowanej broni postaci
        cpRedCharacterWeaponRepository.save(characterWeaponToUpdate);

        return CustomReturnables.getOkResponseMap("Broń postaci została pomyślnie zmodyfikowana");
    }

    public Map<String, Object> deleteCharacterWeapon(Long characterWeaponId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Czy istnieje broń postaci o podanym ID
        CpRedCharacterWeapon characterWeapon = cpRedCharacterWeaponRepository.findById(characterWeaponId)
                .orElseThrow(() -> new ResourceNotFoundException("Broń postaci o podanym ID nie została znaleziona."));

        // Czy istnieje character o podanym ID
        CpRedCharacters character = cpRedCharactersRepository.findById(characterWeapon.getCharacter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postać o podanym ID nie została znaleziona."));

        // Czy istnieje broń o podanym ID
        CpRedWeapons weapon = cpRedWeaponsRepository.findById(characterWeapon.getBaseWeapon().getId())
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
                throw new ResourceNotFoundException("Tylko GM może usuwać bronie postaci NPC.");
            }
        }

        // Czy gra jest aktywna
        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new IllegalStateException("Gra do której należy postać nie jest aktywna.");
        }

        cpRedCharacterWeaponRepository.deleteById(characterWeaponId);

        return CustomReturnables.getOkResponseMap("Broń postaci została pomyślnie usunięta");
    }

    public Map<String, Object> getAllCharacterWeapons() {
        List<CpRedCharacterWeapon> allCharacterWeapons = cpRedCharacterWeaponRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Wszystkie bronie postaci pobrane pomyślnie");
        response.put("allCharacterWeapons", allCharacterWeapons);
        return response;
    }
}

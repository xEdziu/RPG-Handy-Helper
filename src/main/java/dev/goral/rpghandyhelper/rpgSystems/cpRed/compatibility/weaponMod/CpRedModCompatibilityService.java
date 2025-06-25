package dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.weaponMod;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeaponMods.CpRedCustomWeaponMods;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeaponMods.CpRedCustomWeaponModsRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeapons.CpRedCustomWeapons;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeapons.CpRedCustomWeaponsRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weaponMods.CpRedWeaponMods;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weaponMods.CpRedWeaponModsRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons.CpRedWeapons;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons.CpRedWeaponsRepository;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.security.exceptions.ResourceNotFoundException;
import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.user.UserRepository;
import dev.goral.rpghandyhelper.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedModCompatibilityService {
    private final CpRedModCompatibilityRepository weaponModRepository;
    private final UserRepository userRepository;
    private final CpRedCustomWeaponsRepository customWeaponRepository;
    private final CpRedWeaponsRepository weaponRepository;
    private final CpRedCustomWeaponModsRepository customWeaponModsRepository;
    private final CpRedWeaponModsRepository weaponModsRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;

    public Map<String, Object> getAllCompatibility() {

        List<dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.weaponMod.CpRedModCompatibilityDTO> allWeaponModCompatibility = weaponModRepository.findAll()
                .stream()
                .map(cpRedModCompatibility -> new dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.weaponMod.CpRedModCompatibilityDTO(
                        cpRedModCompatibility.getId(),
                        cpRedModCompatibility.getWeaponId(),
                        cpRedModCompatibility.getModId(),
                        cpRedModCompatibility.isWeaponCustom(),
                        cpRedModCompatibility.isModCustom()

                )).toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Lista kompatybilności modyfikacji została pobrana");
        response.put("weaponModCompatibility", allWeaponModCompatibility);
        return response;
    }

    public Map<String, Object> getCompatibilityById(Long modCompatibilityId) {
        CpRedModCompatibility compatibility = weaponModRepository.findById(modCompatibilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Kompatybilność modyfikacji o podanym id nie istnieje."));

        dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.weaponMod.CpRedModCompatibilityDTO compatibilityDTO = new dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.weaponMod.CpRedModCompatibilityDTO(
                compatibility.getId(),
                compatibility.getWeaponId(),
                compatibility.getModId(),
                compatibility.isWeaponCustom(),
                compatibility.isModCustom()
        );

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Kompatybilność modyfikacji została pobrana");
        response.put("weaponModCompatibility", compatibilityDTO);
        return response;
    }

    public Map<String, Object> getCompatibilityByWeaponId(GetModCompatibilityByWeaponRequest getModCompatibilityByWeaponRequest) {
        List<dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.weaponMod.CpRedModCompatibilityDTO> compatibilityByWeapon = weaponModRepository.findAllByWeaponIdAndIsWeaponCustom(
                getModCompatibilityByWeaponRequest.getWeaponId(),
                getModCompatibilityByWeaponRequest.isWeaponCustom()
        ).stream().map(cpRedModCompatibility -> new dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.weaponMod.CpRedModCompatibilityDTO(
                cpRedModCompatibility.getId(),
                cpRedModCompatibility.getWeaponId(),
                cpRedModCompatibility.getModId(),
                cpRedModCompatibility.isWeaponCustom(),
                cpRedModCompatibility.isModCustom()
        )).toList();

        if (getModCompatibilityByWeaponRequest.isWeaponCustom()){
            // Sprawdzenie, czy podana customowa broń istnieje
            if (!customWeaponRepository.existsById(getModCompatibilityByWeaponRequest.getWeaponId())) {
                throw new ResourceNotFoundException("Podana customowa broń nie istnieje.");
            }
        } else {
            // Sprawdzenie, czy podana broń istnieje
            if (!weaponRepository.existsById(getModCompatibilityByWeaponRequest.getWeaponId())) {
                throw new ResourceNotFoundException("Podana broń nie istnieje.");
            }
        }

        if (compatibilityByWeapon.isEmpty()) {
            throw new ResourceNotFoundException("Nie znaleziono kompatybilności modyfikacji dla podanej broni.");
        }

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Kompatybilność modyfikacji dla broni została pobrana");
        response.put("weaponModCompatibility", compatibilityByWeapon);
        return response;
    }

    public Map<String, Object> getCompatibilityByModId(GetModCompatibilityByModRequest getModCompatibilityByModRequest) {
        List<CpRedModCompatibilityDTO> compatibilityByMod = weaponModRepository.findAllByModIdAndIsModCustom(
                getModCompatibilityByModRequest.getModId(),
                getModCompatibilityByModRequest.isModCustom()
        ).stream().map(cpRedModCompatibility -> new CpRedModCompatibilityDTO(
                cpRedModCompatibility.getId(),
                cpRedModCompatibility.getWeaponId(),
                cpRedModCompatibility.getModId(),
                cpRedModCompatibility.isWeaponCustom(),
                cpRedModCompatibility.isModCustom()
        )).toList();

        if (getModCompatibilityByModRequest.isModCustom()){
            // Sprawdzenie, czy podana customowa amunicja istnieje
            if (!customWeaponModsRepository.existsById(getModCompatibilityByModRequest.getModId())) {
                throw new ResourceNotFoundException("Podana customowa modyfikacja nie istnieje.");
            }
        } else {
            // Sprawdzenie, czy podana amunicja istnieje
            if (!weaponModsRepository.existsById(getModCompatibilityByModRequest.getModId())) {
                throw new ResourceNotFoundException("Podana modyfikacja nie istnieje.");
            }
        }

        if (compatibilityByMod.isEmpty()) {
            throw new ResourceNotFoundException("Nie znaleziono kompatybilności broni dla podanej modyfikacji.");
        }

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Kompatybilność modyfikacji dla broni została pobrana");
        response.put("weaponModCompatibility", compatibilityByMod);
        return response;
    }

    public Map<String, Object> addCompatibility(AddModCompatibilityRequest addRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdzenie, czy podano wszystkie wymagane pola
        if (addRequest.getModId() == -1L || addRequest.getWeaponId() == -1L) {
            throw new IllegalStateException("Nie wszystkie pola zostały wypełnione.");
        }

        // Sprawdzenie, podana modyfikacja istnieje
        if (addRequest.isModCustom()){
            if (!customWeaponModsRepository.existsById(addRequest.getModId())) {
                throw new ResourceNotFoundException("Podana customowa modyfikacja nie istnieje.");
            }
        } else {
            if (!weaponModsRepository.existsById(addRequest.getModId())) {
                throw new ResourceNotFoundException("Podana modyfikacja nie istnieje.");
            }
        }

        // Sprawdzenie, podana broń istnieje
        if (addRequest.isWeaponCustom()){
            if (!customWeaponRepository.existsById(addRequest.getWeaponId())) {
                throw new ResourceNotFoundException("Podana broń nie istnieje.");
            }

        } else {
            if (!weaponRepository.existsById(addRequest.getWeaponId())) {
                throw new ResourceNotFoundException("Podana broń nie istnieje.");
            }
        }

        // Sprawdzenie, czy już istnieje kompatybilność
        if (weaponModRepository.existsByWeaponIdAndModIdAndIsWeaponCustomAndIsModCustom(
                addRequest.getWeaponId(),
                addRequest.getModId(),
                addRequest.isWeaponCustom(),
                addRequest.isModCustom()
        )) {
            throw new IllegalStateException("Kompatybilność modyfikacji już istnieje.");
        }

        // Dodawanie kompatybilności przedmiotów z podręcznika
        if (!addRequest.isWeaponCustom() && !addRequest.isModCustom()) {
            // Sprawdzenie, czy użytkownik jest administratorem
            if (currentUser.getRole() != UserRole.ROLE_ADMIN) {
                throw new IllegalStateException("Tylko admin może dodawać kompatybilność pomiędzy zwykłymi przedmiotami.");
            }
        } else {
            // Sprawdzenie, czy użytkownik jest GM w grze, których customowe przedmioty są podane
            Game weaponGame = null;
            Game modGame = null;

            if (addRequest.isWeaponCustom()){
                // Pobranie customowej broni
                CpRedCustomWeapons customWeapon = customWeaponRepository.findById(addRequest.getWeaponId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana customowa broń nie istnieje."));
                // Pobranie gry, do której należy broń
                weaponGame = gameRepository.findById(customWeapon.getGameId().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Gra do, której należy broń nie istnieje."));
                // Sprawdzenie, czy użytkownik jest w grze
                GameUsers weaponGameUser = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), weaponGame.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Użytkownik nie należy do gry, do której należy broń."));
                // Sprawdzenie, czy użytkownik jest GM w grze, do której należy broń
                if (weaponGameUser.getRole() != GameUsersRole.GAMEMASTER) {
                    throw new IllegalStateException("Użytkownik nie jest GM w grze, do której należy broń.");
                }
                // Czy gra jest aktywna
                if (weaponGame.getStatus() != GameStatus.ACTIVE) {
                    throw new IllegalStateException("Gra, do której należy broń, nie jest aktywna.");
                }
                // Czy broń można modyfikować
                if (!customWeapon.getIsModifiable()){
                    throw new IllegalStateException("Ta broń nie może być modyfikowana");
                }
            } else {
                CpRedWeapons weapon = weaponRepository.findById(addRequest.getWeaponId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana broń nie istnieje."));
                // Czy broń można modyfikować
                if (!weapon.getIsModifiable()){
                    throw new IllegalStateException("Ta broń nie może być modyfikowana");
                }
            }
            if (addRequest.isModCustom()){
                CpRedCustomWeaponMods customMod = customWeaponModsRepository.findById(addRequest.getModId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana modyfikacja nie istnieje."));
                modGame = gameRepository.findById(customMod.getGameId().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Gra do, której należy modyfikacja nie istnieje."));
                GameUsers modGameUser = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), modGame.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Użytkownik nie należy do gry, do której należy modyfikacja."));
                // Sprawdź, czy użytkownik jest GM
                if (modGameUser.getRole() != GameUsersRole.GAMEMASTER) {
                    throw new IllegalStateException("Użytkownik nie jest GM w grze, do której należy modyfikacja.");
                }
                if (modGame.getStatus() != GameStatus.ACTIVE) {
                    throw new IllegalStateException("Gra, do której należy modyfikacja, nie jest aktywna.");
                }
            } else {
                CpRedWeaponMods mod = weaponModsRepository.findById(addRequest.getModId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana modyfikacja nie istnieje."));
            }

            // Sprawdzić, czy jeżeli dwa customy to, czy są w tej samej grze
            if (addRequest.isWeaponCustom() && addRequest.isModCustom()) {
                if (!weaponGame.getId().equals(modGame.getId())) {
                    throw new IllegalStateException("Nie można dodać kompatybilności pomiędzy przedmiotami z różnych gier.");
                }
            }
        }

        // Dodawanie kompatybilności przedmiotów customowych
        CpRedModCompatibility newCompatibility = new CpRedModCompatibility(
                null,
                addRequest.getWeaponId(),
                addRequest.getModId(),
                addRequest.isWeaponCustom(),
                addRequest.isModCustom()
        );
        // Zapisanie kompatybilności do bazy danych
        weaponModRepository.save(newCompatibility);
        return CustomReturnables.getOkResponseMap("Kompatybilność modyfikacji została dodana.");
    }

    public Map<String, Object> updateCompatibility(Long modCompatibilityId, AddModCompatibilityRequest updateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdzenie, czy kompatybilność istnieje
        CpRedModCompatibility compatibilityToUpdate = weaponModRepository.findById(modCompatibilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Kompatybilność modyfikacji o podanym id nie istnieje."));

        // Sprawdzenie, czy podano wszystkie wymagane pola
        if (updateRequest.getModId() == -1L || updateRequest.getWeaponId() == -1L) {
            throw new IllegalStateException("Nie wszystkie pola zostały wypełnione.");
        }

        // Sprawdzenie, podana modyfikacja istnieje
        if (updateRequest.isModCustom()){
            if (!customWeaponModsRepository.existsById(updateRequest.getModId())) {
                throw new ResourceNotFoundException("Podana modyfikacja nie istnieje.");
            }
        } else {
            if (!weaponModRepository.existsById(updateRequest.getModId())) {
                throw new ResourceNotFoundException("Podana modyfikacja nie istnieje.");
            }
        }

        // Sprawdzenie, podana broń istnieje
        if (updateRequest.isWeaponCustom()){
            if (!customWeaponRepository.existsById(updateRequest.getWeaponId())) {
                throw new ResourceNotFoundException("Podana broń nie istnieje.");
            }
        } else {
            if (!weaponRepository.existsById(updateRequest.getWeaponId())) {
                throw new ResourceNotFoundException("Podana broń nie istnieje.");
            }
        }

        // Sprawdzenie, czy już istnieje kompatybilność
        if (weaponModRepository.existsByWeaponIdAndModIdAndIsWeaponCustomAndIsModCustom(
                updateRequest.getWeaponId(),
                updateRequest.getModId(),
                updateRequest.isWeaponCustom(),
                updateRequest.isModCustom()
        )) {
            throw new IllegalStateException("Kompatybilność modyfikacji już istnieje.");
        }

        // Dodawanie kompatybilności przedmiotów z podręcznika
        if (!updateRequest.isWeaponCustom() && !updateRequest.isModCustom()) {
            // Sprawdzenie, czy użytkownik jest administratorem
            if (currentUser.getRole() != UserRole.ROLE_ADMIN) {
                throw new IllegalStateException("Tylko admin może dodawać kompatybilność pomiędzy zwykłymi przedmiotami.");
            }
        } else {
            // Sprawdzenie, czy użytkownik jest GM w grze, których customowe przedmioty są podane
            Game weaponGame = null;
            Game modGame = null;

            if (updateRequest.isWeaponCustom()){
                // Pobranie customowej broni
                CpRedCustomWeapons customWeapon = customWeaponRepository.findById(updateRequest.getWeaponId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana customowa broń nie istnieje."));
                // Pobranie gry, do której należy broń
                weaponGame = gameRepository.findById(customWeapon.getGameId().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Gra do, której należy broń nie istnieje."));
                // Sprawdzenie, czy użytkownik jest w grze
                GameUsers weaponGameUser = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), weaponGame.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Użytkownik nie należy do gry, do której należy broń."));
                // Sprawdzenie, czy użytkownik jest GM w grze, do której należy broń
                if (weaponGameUser.getRole() != GameUsersRole.GAMEMASTER) {
                    throw new IllegalStateException("Użytkownik nie jest GM w grze, do której należy broń.");
                }
                // Czy gra jest aktywna
                if (weaponGame.getStatus() != GameStatus.ACTIVE) {
                    throw new IllegalStateException("Gra, do której należy broń, nie jest aktywna.");
                }
            } else {
                CpRedWeapons weapon = weaponRepository.findById(updateRequest.getWeaponId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana broń nie istnieje."));
            }
            if (updateRequest.isModCustom()){
                CpRedCustomWeaponMods customMod = customWeaponModsRepository.findById(updateRequest.getModId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana modyfikacja nie istnieje."));
                modGame = gameRepository.findById(customMod.getGameId().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Gra do, której należy modyfikacja nie istnieje."));
                GameUsers modGameUser = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), modGame.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Użytkownik nie należy do gry, do której należy modyfikacja."));
                // Sprawdź, czy użytkownik jest GM
                if (modGameUser.getRole() != GameUsersRole.GAMEMASTER) {
                    throw new IllegalStateException("Użytkownik nie jest GM w grze, do której należy modyfikacja.");
                }
                if (modGame.getStatus() != GameStatus.ACTIVE) {
                    throw new IllegalStateException("Gra, do której należy modyfikacja, nie jest aktywna.");
                }
            } else {
                CpRedWeaponMods mod = weaponModsRepository.findById(updateRequest.getModId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana modyfikacja nie istnieje."));
            }

            // Sprawdzić, czy jeżeli dwa customy to, czy są w tej samej grze
            if (updateRequest.isWeaponCustom() && updateRequest.isModCustom()) {
                if (!weaponGame.getId().equals(modGame.getId())) {
                    throw new IllegalStateException("Nie można dodać kompatybilności pomiędzy przedmiotami z różnych gier.");
                }
            }
        }

        // Aktualizacja kompatybilności
        compatibilityToUpdate.setModId(updateRequest.getModId());
        compatibilityToUpdate.setWeaponId(updateRequest.getWeaponId());
        compatibilityToUpdate.setWeaponCustom(updateRequest.isWeaponCustom());
        compatibilityToUpdate.setModCustom(updateRequest.isModCustom());

        // Zapisanie zmodyfikowanej kompatybilności do bazy danych
        weaponModRepository.save(compatibilityToUpdate);

        return CustomReturnables.getOkResponseMap("Kompatybilność modyfikacji została zmieniona.");
    }

}

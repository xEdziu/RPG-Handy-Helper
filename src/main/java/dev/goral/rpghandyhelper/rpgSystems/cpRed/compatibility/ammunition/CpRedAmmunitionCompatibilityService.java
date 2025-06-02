package dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.ammunition;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.game.GameRepository;
import dev.goral.rpghandyhelper.game.GameStatus;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsers;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRepository;
import dev.goral.rpghandyhelper.game.gameUsers.GameUsersRole;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunition;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunitionRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeapons.CpRedCustomWeapons;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeapons.CpRedCustomWeaponsRepository;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition.CpRedAmmunition;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition.CpRedAmmunitionRepository;
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
public class CpRedAmmunitionCompatibilityService {
    private final CpRedAmmunitionCompatibilityRepository weaponAmmoRepository;
    private final UserRepository userRepository;
    private final CpRedCustomAmmunitionRepository customAmmunitionRepository;
    private final CpRedCustomWeaponsRepository customWeaponRepository;
    private final CpRedAmmunitionRepository ammunitionRepository;
    private final CpRedWeaponsRepository weaponRepository;
    private final GameRepository gameRepository;
    private final GameUsersRepository gameUsersRepository;


    public Map<String, Object> getAllCompatibility() {

        List<CpRedAmmunitionCompatibilityDTO> allWeaponAmmunitionCompatibility = weaponAmmoRepository.findAll()
                .stream()
                .map(cpRedAmmunitionCompatibility -> new CpRedAmmunitionCompatibilityDTO(
                        cpRedAmmunitionCompatibility.getId(),
                        cpRedAmmunitionCompatibility.getWeaponId(),
                        cpRedAmmunitionCompatibility.getAmmunitionId(),
                        cpRedAmmunitionCompatibility.isWeaponCustom(),
                        cpRedAmmunitionCompatibility.isAmmunitionCustom()

                )).toList();

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Lista kompatybilności amunicji została pobrana");
        response.put("weaponAmmunitionCompatibility", allWeaponAmmunitionCompatibility);
        return response;
    }

    public Map<String, Object> getCompatibilityById(Long ammunitionCompatibilityId) {
        CpRedAmmunitionCompatibility compatibility = weaponAmmoRepository.findById(ammunitionCompatibilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Kompatybilność amunicji o podanym id nie istnieje."));

        CpRedAmmunitionCompatibilityDTO compatibilityDTO = new CpRedAmmunitionCompatibilityDTO(
                compatibility.getId(),
                compatibility.getWeaponId(),
                compatibility.getAmmunitionId(),
                compatibility.isWeaponCustom(),
                compatibility.isAmmunitionCustom()
        );

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Kompatybilność amunicji została pobrana");
        response.put("weaponAmmunitionCompatibility", compatibilityDTO);
        return response;
    }

    public Map<String, Object> getCompatibilityByWeaponId(GetCompatibilityByWeaponRequest getCompatibilityByWeaponRequest) {
        List<CpRedAmmunitionCompatibilityDTO> compatibilityByWeapon = weaponAmmoRepository.findAllByWeaponIdAndIsWeaponCustom(
                getCompatibilityByWeaponRequest.getWeaponId(),
                getCompatibilityByWeaponRequest.isWeaponCustom()
        ).stream().map(cpRedAmmunitionCompatibility -> new CpRedAmmunitionCompatibilityDTO(
                cpRedAmmunitionCompatibility.getId(),
                cpRedAmmunitionCompatibility.getWeaponId(),
                cpRedAmmunitionCompatibility.getAmmunitionId(),
                cpRedAmmunitionCompatibility.isWeaponCustom(),
                cpRedAmmunitionCompatibility.isAmmunitionCustom()
        )).toList();

        if (getCompatibilityByWeaponRequest.isWeaponCustom()){
            // Sprawdzenie, czy podana customowa broń istnieje
            if (!customWeaponRepository.existsById(getCompatibilityByWeaponRequest.getWeaponId())) {
                throw new ResourceNotFoundException("Podana customowa broń nie istnieje.");
            }
        } else {
            // Sprawdzenie, czy podana broń istnieje
            if (!weaponRepository.existsById(getCompatibilityByWeaponRequest.getWeaponId())) {
                throw new ResourceNotFoundException("Podana broń nie istnieje.");
            }
        }

        if (compatibilityByWeapon.isEmpty()) {
            throw new ResourceNotFoundException("Nie znaleziono kompatybilności amunicji dla podanej broni.");
        }

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Kompatybilność amunicji dla broni została pobrana");
        response.put("weaponAmmunitionCompatibility", compatibilityByWeapon);
        return response;
    }

    public Map<String, Object> getCompatibilityByAmmunitionId(GetCompatibilityByAmmunitionRequest getCompatibilityByAmmunitionRequest) {
        List<CpRedAmmunitionCompatibilityDTO> compatibilityByAmmunition = weaponAmmoRepository.findAllByAmmunitionIdAndIsAmmunitionCustom(
                getCompatibilityByAmmunitionRequest.getAmmunitionId(),
                getCompatibilityByAmmunitionRequest.isAmmunitionCustom()
        ).stream().map(cpRedAmmunitionCompatibility -> new CpRedAmmunitionCompatibilityDTO(
                cpRedAmmunitionCompatibility.getId(),
                cpRedAmmunitionCompatibility.getWeaponId(),
                cpRedAmmunitionCompatibility.getAmmunitionId(),
                cpRedAmmunitionCompatibility.isWeaponCustom(),
                cpRedAmmunitionCompatibility.isAmmunitionCustom()
        )).toList();

        if (getCompatibilityByAmmunitionRequest.isAmmunitionCustom()){
            // Sprawdzenie, czy podana customowa amunicja istnieje
            if (!customAmmunitionRepository.existsById(getCompatibilityByAmmunitionRequest.getAmmunitionId())) {
                throw new ResourceNotFoundException("Podana customowa amunicja nie istnieje.");
            }
        } else {
            // Sprawdzenie, czy podana amunicja istnieje
            if (!ammunitionRepository.existsById(getCompatibilityByAmmunitionRequest.getAmmunitionId())) {
                throw new ResourceNotFoundException("Podana amunicja nie istnieje.");
            }
        }

        if (compatibilityByAmmunition.isEmpty()) {
            throw new ResourceNotFoundException("Nie znaleziono kompatybilności broni dla podanej amunicji.");
        }

        Map<String, Object> response = CustomReturnables.getOkResponseMap("Kompatybilność amunicji dla broni została pobrana");
        response.put("weaponAmmunitionCompatibility", compatibilityByAmmunition);
        return response;
    }

    public Map<String, Object> addCompatibility(AddAmmunitionCompatibilityRequest addRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdzenie, czy podano wszystkie wymagane pola
        if (addRequest.getAmmunitionId() == -1L || addRequest.getWeaponId() == -1L) {
            throw new IllegalStateException("Nie wszystkie pola zostały wypełnione.");
        }

        // Sprawdzenie, podana amunicja istnieje
        if (addRequest.isAmmunitionCustom()){
            if (!customAmmunitionRepository.existsById(addRequest.getAmmunitionId())) {
                throw new ResourceNotFoundException("Podana customowa amunicja nie istnieje.");
            }
        } else {
            if (!ammunitionRepository.existsById(addRequest.getAmmunitionId())) {
                throw new ResourceNotFoundException("Podana amunicja nie istnieje.");
            }
        }

        // Sprawdzenie, podana broń istnieje
        if (addRequest.isWeaponCustom()){
            if (!customWeaponRepository.existsById(addRequest.getWeaponId())) {
                throw new ResourceNotFoundException("Podana customowa broń nie istnieje.");
            }
        } else {
            if (!weaponRepository.existsById(addRequest.getWeaponId())) {
                throw new ResourceNotFoundException("Podana broń nie istnieje.");
            }
        }

        // Sprawdzenie, czy już istnieje kompatybilność
        if (weaponAmmoRepository.existsByWeaponIdAndAmmunitionIdAndIsWeaponCustomAndIsAmmunitionCustom(
                addRequest.getWeaponId(),
                addRequest.getAmmunitionId(),
                addRequest.isWeaponCustom(),
                addRequest.isAmmunitionCustom()
        )) {
            throw new IllegalStateException("Kompatybilność amunicji już istnieje.");
        }

        // Dodawanie kompatybilności przedmiotów z podręcznika
        if (!addRequest.isWeaponCustom() && !addRequest.isAmmunitionCustom()) {
            // Sprawdzenie, czy użytkownik jest administratorem
            if (currentUser.getRole() != UserRole.ROLE_ADMIN) {
                throw new IllegalStateException("Tylko admin może dodawać kompatybilność pomiędzy zwykłymi przedmiotami.");
            }
        } else {
            // Sprawdzenie, czy użytkownik jest GM w grze, których customowe przedmioty są podane
            Game weaponGame = null;
            Game ammunitionGame = null;

            if (addRequest.isWeaponCustom()){
                // Pobranie customowej broni
                CpRedCustomWeapons customWeapon = customWeaponRepository.findById(addRequest.getWeaponId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana broń nie istnieje."));
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
                CpRedWeapons weapon = weaponRepository.findById(addRequest.getWeaponId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana broń nie istnieje."));
            }
            if (addRequest.isAmmunitionCustom()){
                CpRedCustomAmmunition customAmmunition = customAmmunitionRepository.findById(addRequest.getAmmunitionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana amunicja nie istnieje."));
                ammunitionGame = gameRepository.findById(customAmmunition.getGameId().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Gra do, której należy amunicja nie istnieje."));
                GameUsers ammunitionGameUser = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), ammunitionGame.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Użytkownik nie należy do gry, do której należy amunicja."));
                // Sprawdź, czy użytkownik jest GM
                if (ammunitionGameUser.getRole() != GameUsersRole.GAMEMASTER) {
                    throw new IllegalStateException("Użytkownik nie jest GM w grze, do której należy amunicja.");
                }
                if (ammunitionGame.getStatus() != GameStatus.ACTIVE) {
                    throw new IllegalStateException("Gra, do której należy amunicja, nie jest aktywna.");
                }
            } else {
                CpRedAmmunition ammunition = ammunitionRepository.findById(addRequest.getAmmunitionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana amunicja nie istnieje."));
            }

            // Sprawdzić, czy jeżeli dwa customy to, czy są w tej samej grze
            if (addRequest.isWeaponCustom() && addRequest.isAmmunitionCustom()) {
                if (!weaponGame.getId().equals(ammunitionGame.getId())) {
                    throw new IllegalStateException("Nie można dodać kompatybilności pomiędzy przedmiotami z różnych gier.");
                }
            }
        }

        // Dodawanie kompatybilności przedmiotów customowych
        CpRedAmmunitionCompatibility newCompatibility = new CpRedAmmunitionCompatibility(
                null,
                addRequest.getWeaponId(),
                addRequest.getAmmunitionId(),
                addRequest.isWeaponCustom(),
                addRequest.isAmmunitionCustom()
        );
        // Zapisanie kompatybilności do bazy danych
        weaponAmmoRepository.save(newCompatibility);
        return CustomReturnables.getOkResponseMap("Kompatybilność amunicji została dodana.");
    }

    // Aktualizacja kompatybilności amunicji
    public Map<String, Object> updateCompatibility(Long ammunitionCompatibilityId, AddAmmunitionCompatibilityRequest updateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        // Sprawdzenie, czy kompatybilność istnieje
        CpRedAmmunitionCompatibility compatibilityToUpdate = weaponAmmoRepository.findById(ammunitionCompatibilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Kompatybilność amunicji o podanym id nie istnieje."));

        // Sprawdzenie, czy podano wszystkie wymagane pola
        if (updateRequest.getAmmunitionId() == -1L || updateRequest.getWeaponId() == -1L) {
            throw new IllegalStateException("Nie wszystkie pola zostały wypełnione.");
        }

        // Sprawdzenie, podana amunicja istnieje
        if (updateRequest.isAmmunitionCustom()){
            if (!customAmmunitionRepository.existsById(updateRequest.getAmmunitionId())) {
                throw new ResourceNotFoundException("Podana amunicja nie istnieje.");
            }
        } else {
            if (!ammunitionRepository.existsById(updateRequest.getAmmunitionId())) {
                throw new ResourceNotFoundException("Podana amunicja nie istnieje.");
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
        if (weaponAmmoRepository.existsByWeaponIdAndAmmunitionIdAndIsWeaponCustomAndIsAmmunitionCustom(
                updateRequest.getWeaponId(),
                updateRequest.getAmmunitionId(),
                updateRequest.isWeaponCustom(),
                updateRequest.isAmmunitionCustom()
        )) {
            throw new IllegalStateException("Kompatybilność amunicji już istnieje.");
        }

        // Dodawanie kompatybilności przedmiotów z podręcznika
        if (!updateRequest.isWeaponCustom() && !updateRequest.isAmmunitionCustom()) {
            // Sprawdzenie, czy użytkownik jest administratorem
            if (currentUser.getRole() != UserRole.ROLE_ADMIN) {
                throw new IllegalStateException("Tylko admin może dodawać kompatybilność pomiędzy zwykłymi przedmiotami.");
            }
        } else {
            // Sprawdzenie, czy użytkownik jest GM w grze, których customowe przedmioty są podane
            Game weaponGame = null;
            Game ammunitionGame = null;

            if (updateRequest.isWeaponCustom()){
                // Pobranie customowej broni
                CpRedCustomWeapons customWeapon = customWeaponRepository.findById(updateRequest.getWeaponId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana broń nie istnieje."));
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
            if (updateRequest.isAmmunitionCustom()){
                CpRedCustomAmmunition customAmmunition = customAmmunitionRepository.findById(updateRequest.getAmmunitionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana amunicja nie istnieje."));
                ammunitionGame = gameRepository.findById(customAmmunition.getGameId().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Gra do, której należy amunicja nie istnieje."));
                GameUsers ammunitionGameUser = gameUsersRepository.findGameUsersByUserIdAndGameId(currentUser.getId(), ammunitionGame.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Użytkownik nie należy do gry, do której należy amunicja."));
                // Sprawdź, czy użytkownik jest GM
                if (ammunitionGameUser.getRole() != GameUsersRole.GAMEMASTER) {
                    throw new IllegalStateException("Użytkownik nie jest GM w grze, do której należy amunicja.");
                }
                if (ammunitionGame.getStatus() != GameStatus.ACTIVE) {
                    throw new IllegalStateException("Gra, do której należy amunicja, nie jest aktywna.");
                }
            } else {
                CpRedAmmunition ammunition = ammunitionRepository.findById(updateRequest.getAmmunitionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Podana amunicja nie istnieje."));
            }

            // Sprawdzić, czy jeżeli dwa customy to, czy są w tej samej grze
            if (updateRequest.isWeaponCustom() && updateRequest.isAmmunitionCustom()) {
                if (!weaponGame.getId().equals(ammunitionGame.getId())) {
                    throw new IllegalStateException("Nie można dodać kompatybilności pomiędzy przedmiotami z różnych gier.");
                }
            }
        }

        // Aktualizacja kompatybilności
        compatibilityToUpdate.setAmmunitionId(updateRequest.getAmmunitionId());
        compatibilityToUpdate.setWeaponId(updateRequest.getWeaponId());
        compatibilityToUpdate.setWeaponCustom(updateRequest.isWeaponCustom());
        compatibilityToUpdate.setAmmunitionCustom(updateRequest.isAmmunitionCustom());

        // Zapisanie zmodyfikowanej kompatybilności do bazy danych
        weaponAmmoRepository.save(compatibilityToUpdate);

        return CustomReturnables.getOkResponseMap("Kompatybilność amunicji została zmieniona.");
    }
}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weaponMods;

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
public class CpRedWeaponModsService {
    private final CpRedWeaponModsRepository cpRedWeaponModsRepository;
    private final UserRepository userRepository;

    public Map<String, Object> getAllWeaponMods() {
        List<CpRedWeaponMods> mods = cpRedWeaponModsRepository.findAll();
        List<CpRedWeaponModsDTO> modsDTO = mods.stream().map(mod ->
                new CpRedWeaponModsDTO(
                        mod.getName(),
                        mod.getPrice(),
                        mod.getSize(),
                        mod.getAvailability().toString(),
                        mod.getDescription()

                )).toList();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano modyfikacje broni");
        response.put("weaponMods", modsDTO);
        return response;
    }

    public Map<String, Object> getWeaponModById(Long weaponModId) {
        CpRedWeaponModsDTO weaponMod = cpRedWeaponModsRepository.findById(weaponModId)
                .map(cpRedWeaponMods -> new CpRedWeaponModsDTO(
                        cpRedWeaponMods.getName(),
                        cpRedWeaponMods.getPrice(),
                        cpRedWeaponMods.getSize(),
                        cpRedWeaponMods.getAvailability().toString(),
                        cpRedWeaponMods.getDescription()
                )).orElseThrow(() -> new ResourceNotFoundException("Modyfikacja broni o id " + weaponModId + " nie została znaleziona"));
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano modyfikację broni");
        response.put("weaponMod", weaponMod);
        return response;
    }

    public Map<String, Object> getAllWeaponModsForAdmin() {
        List<CpRedWeaponMods> allWeaponMods = cpRedWeaponModsRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano modyfikacje broni");
        response.put("weaponMods", allWeaponMods);
        return response;
    }

    public Map<String, Object> addWeaponMod(CpRedWeaponMods cpRedWeaponMods) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do przeglądania tej sekcji.");
        }
        if (cpRedWeaponMods.getName() == null ||
                cpRedWeaponMods.getPrice() == -1 ||
                cpRedWeaponMods.getSize() == -1 ||
                cpRedWeaponMods.getAvailability() == null ||
                cpRedWeaponMods.getDescription() == null) {
            throw new IllegalStateException("Nie podano wszystkich parametrów");
        }

        if (cpRedWeaponModsRepository.existsByName(cpRedWeaponMods.getName())) {
            throw new IllegalStateException("Modyfikacja o tej nazwie już istnieje.");
        }
        if (cpRedWeaponMods.getName().isEmpty() || cpRedWeaponMods.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa modyfikacji nie może być pusta.");
        }
        if (cpRedWeaponMods.getName().length() > 255) {
            throw new IllegalStateException("Nazwa modyfikacji nie może być dłuższa niż 255 znaków.");
        }
        if (cpRedWeaponMods.getPrice() < 0) {
            throw new IllegalStateException("Cena modyfikacji broni musi być większa lub równa 0");
        }
        if (cpRedWeaponMods.getSize() <= 0) {
            throw new IllegalStateException("Rozmiar modyfikacji broni musi być większy od 0");
        }
        String description = cpRedWeaponMods.getDescription();
        if (cpRedWeaponMods.getDescription() != null) {
            if (cpRedWeaponMods.getDescription().isEmpty() || cpRedWeaponMods.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis modyfikacji nie może być pusty.");
            }
            if (cpRedWeaponMods.getDescription().length() > 500) {
                throw new IllegalStateException("Opis modyfikacji nie może być dłuższy niż 500 znaków.");
            }
        }
            CpRedWeaponMods newWeaponMod = new CpRedWeaponMods(
                    null,
                    cpRedWeaponMods.getName(),
                    cpRedWeaponMods.getPrice(),
                    cpRedWeaponMods.getSize(),
                    cpRedWeaponMods.getAvailability(),
                    cpRedWeaponMods.getDescription()
            );
            cpRedWeaponModsRepository.save(newWeaponMod);
            return CustomReturnables.getOkResponseMap("Dodano modyfikację broni");
    }


    public Map<String, Object> updateWeaponMod(Long weaponModId, CpRedWeaponMods cpRedWeaponMods) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do dodawania wszczepów.");
        }
        CpRedWeaponMods modToUpdate = cpRedWeaponModsRepository.findById(weaponModId)
                .orElseThrow(() -> new ResourceNotFoundException("Modyfikacja o id " + weaponModId + " nie istnieje"));
        if (cpRedWeaponMods.getName() != null) {
            if (cpRedWeaponModsRepository.existsByName(cpRedWeaponMods.getName())) {
                throw new IllegalStateException("Modyfikacja o tej nazwie już istnieje.");
            }
            if (cpRedWeaponMods.getName().isEmpty() || cpRedWeaponMods.getName().trim().isEmpty()) {
                throw new IllegalStateException("Nazwa modyfikacji nie może być pusta.");
            }
            if (cpRedWeaponMods.getName().length() > 255) {
                throw new IllegalStateException("Nazwa modyfikacji nie może być dłuższa niż 255 znaków.");
            }
            modToUpdate.setName(cpRedWeaponMods.getName());
        }
        if (cpRedWeaponMods.getSize() != -1) {
            if (cpRedWeaponMods.getSize() < 0) {
                throw new IllegalStateException("Rozmiar nie może być ujemny.");
            }
            modToUpdate.setSize(cpRedWeaponMods.getSize());
        }

        if(cpRedWeaponMods.getPrice() != -1) {
            if (cpRedWeaponMods.getPrice() < 0) {
                throw new IllegalStateException("Cena nie może być ujemna.");
            }
            modToUpdate.setPrice(cpRedWeaponMods.getPrice());
        }
        if (cpRedWeaponMods.getAvailability() != null) {
            modToUpdate.setAvailability(cpRedWeaponMods.getAvailability());
        }
        if (cpRedWeaponMods.getDescription() != null) {
            if (cpRedWeaponMods.getDescription().isEmpty() || cpRedWeaponMods.getDescription().trim().isEmpty()) {
                throw new IllegalStateException("Opis modyfikacji nie może być pusty.");
            }
            if (cpRedWeaponMods.getDescription().length() > 500) {
                throw new IllegalStateException("Opis modyfikacji nie może być dłuższy niż 500 znaków.");
            }
            modToUpdate.setDescription(cpRedWeaponMods.getDescription());
        }
        cpRedWeaponModsRepository.save(modToUpdate);
        return CustomReturnables.getOkResponseMap("Zaktualizowano modyfikację broni");
    }
}

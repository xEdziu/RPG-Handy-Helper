package dev.goral.rpgmanager.rpgSystems.cpRed.manual.weaponMods;

import dev.goral.rpgmanager.rpgSystems.cpRed.manual.armors.CpRedArmors;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.armors.CpRedArmorsDTO;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import dev.goral.rpgmanager.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CpRedWeaponModsService {
    private final CpRedWeaponModsRepository cpRedWeaponModsRepository;
    private final UserRepository userRepository;

    // Pobierz wszystkie modyfikacje broni
    public Map<String, Object> getAllWeaponMods() {
        List<CpRedWeaponMods> mods= cpRedWeaponModsRepository.findAll();
        List<CpRedWeaponModsDTO> modsDTO = mods.stream().map( mod->
                new CpRedWeaponModsDTO(
                        mod.getName(),
                        mod.getPrice(),
                        mod.getSize(),
                        mod.getAvailability().toString(),
                        mod.getDescription()

                )).toList();
        if(modsDTO.isEmpty()){
            return CustomReturnables.getOkResponseMap("Brak modyfikacji broni");
        }
        Map<String,Object> response = CustomReturnables.getOkResponseMap("Pobrano modyfikacje broni");
        response.put("weaponMods", modsDTO);
        return response;
    }

    // Pobierz modyfikacje broni po id
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

    // Pobierz wszystkie modyfikacje broni dla admina
    public Map<String, Object> getAllWeaponModsForAdmin() {
        List<CpRedWeaponMods> allWeaponMods = cpRedWeaponModsRepository.findAll();
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano modyfikacje broni");
        response.put("weaponMods", allWeaponMods);
        return response;
    }

    // Dodaj modyfikacje broni
    public Map<String, Object> addWeaponMod(CpRedWeaponMods cpRedWeaponMods) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));
        if (!currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new IllegalStateException("Nie masz uprawnień do przeglądania tej sekcji.");
        }
        if (cpRedWeaponMods.getName() == null ||
                cpRedWeaponMods.getPrice() == 0 ||
                cpRedWeaponMods.getSize() == 0 ||
                cpRedWeaponMods.getAvailability() == null ||
                cpRedWeaponMods.getDescription() == null) {
            throw new IllegalStateException("Nie podano wszystkich parametrów");
        }

        if(cpRedWeaponModsRepository.existsByName(cpRedWeaponMods.getName())) {
            throw new IllegalStateException("Modyfikacja o tej nazwie już istnieje.");
        }
        if (cpRedWeaponMods.getName().isEmpty() || cpRedWeaponMods.getName().trim().isEmpty()) {
            throw new IllegalStateException("Nazwa modfikacji nie może być pusta.");
        }
        if (cpRedWeaponMods.getName().length() > 255) {
            throw new IllegalStateException("Nazwa modfikacji nie może być dłuższa niż 255 znaków.");
        }

        if( cpRedWeaponMods.getPrice() < 0) {
            throw new IllegalStateException("Cena modyfikacji broni musi być większa lub równa 0");
        }
        if( cpRedWeaponMods.getSize() <= 0) {
            throw new IllegalStateException("Rozmiar modyfikacji broni musi być większy od 0");
        }
        String description = cpRedWeaponMods.getDescription();
        if(description.length() > 1000) {
            throw new IllegalStateException("Opis nie może być dłuższy niż 1000 znaków");
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
//
//    // Modyfikuj modyfikacje broni
//    public Map<String, Object> updateWeaponMod(Long weaponModId, CpRedWeaponMods cpRedWeaponMods) {
//
//    }
}

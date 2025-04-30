package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition;

import dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition.customWeaponAmmunitionCompatibility.CpRedCustomWeaponAmmunitionCompatibility;
import dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition.customWeaponAmmunitionCompatibility.CpRedCustomWeaponAmmunitionCompatibilityRepository;
import dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition.customWeaponCustomAmmunitionCompatibility.CpRedCustomWeaponCustomAmmunitionCompatibility;
import dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition.customWeaponCustomAmmunitionCompatibility.CpRedCustomWeaponCustomAmmunitionCompatibilityRepository;
import dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition.weaponAmmunitionCompatibility.CpRedWeaponAmmunitionCompatibility;
import dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition.weaponAmmunitionCompatibility.CpRedWeaponAmmunitionCompatibilityRepository;
import dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition.weaponCustomAmmunitionCompatibility.CpRedWeaponCustomAmmunitionCompatibility;
import dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition.weaponCustomAmmunitionCompatibility.CpRedWeaponCustomAmmunitionCompatibilityRepository;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunition;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunitionRepository;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeapons.CpRedCustomWeapons;
import dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeapons.CpRedCustomWeaponsRepository;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.ammunition.CpRedAmmunition;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.ammunition.CpRedAmmunitionRepository;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.weapons.CpRedWeapons;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.weapons.CpRedWeaponsRepository;
import dev.goral.rpgmanager.security.CustomReturnables;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class CpRedAmmunitionCompatibilityService {
    private final CpRedWeaponAmmunitionCompatibilityRepository weaponAmmoRepo;
    private final CpRedWeaponCustomAmmunitionCompatibilityRepository weaponCustomAmmoRepo;
    private final CpRedCustomWeaponAmmunitionCompatibilityRepository customWeaponAmmoRepo;
    private final CpRedCustomWeaponCustomAmmunitionCompatibilityRepository customWeaponCustomAmmoRepo;
    private final UserRepository userRepository;
    private final CpRedAmmunitionRepository ammoRepo;
    private final CpRedCustomAmmunitionRepository customAmmoRepo;
    private final CpRedCustomWeaponsRepository customWeaponRepo;
    private final CpRedWeaponsRepository weaponRepo;


    @Transactional
    public Map<String, Object> addCompatibility( Long weaponId,
                                                 Long ammunitionId,
                                                 boolean isCustomWeapon,
                                                 boolean isCustomAmmo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Zalogowany użytkownik nie został znaleziony."));

        //TODO:
        // Sprawdzenie czy odpowiedni użytkownik chce dokonać zmian
        // Sprawdzenie czy broń istnieje
        // Sprawdzenie czy amunicja istnieje

        if (!isCustomWeapon && !isCustomAmmo) {
            CpRedWeapons weapon = weaponRepo.findById(weaponId)
                    .orElseThrow(() -> new EntityNotFoundException("Broń o podanym id nie została znaleziona."));
            CpRedAmmunition ammo = ammoRepo.findById(ammunitionId)
                    .orElseThrow(() -> new EntityNotFoundException("Amunicja o podanym id nie została znaleziona."));

            CpRedWeaponAmmunitionCompatibility entity = new CpRedWeaponAmmunitionCompatibility(weapon, ammo);
            weaponAmmoRepo.save(entity);
            return CustomReturnables.getOkResponseMap("Dodano kompatybilność Weapon - Ammo.");

        } else if (!isCustomWeapon && isCustomAmmo) {
            CpRedWeapons weapon = weaponRepo.findById(weaponId)
                    .orElseThrow(() -> new EntityNotFoundException("Broń o podanym id nie została znaleziona."));
            CpRedCustomAmmunition ammo = customAmmoRepo.findById(ammunitionId)
                    .orElseThrow(() -> new EntityNotFoundException("Amunicja o podanym id nie została znaleziona."));

            CpRedWeaponCustomAmmunitionCompatibility entity = new CpRedWeaponCustomAmmunitionCompatibility(weapon, ammo);
            weaponCustomAmmoRepo.save(entity);
            return CustomReturnables.getOkResponseMap("Dodano kompatybilność Weapon - CustomAmmo.");

        } else if (isCustomWeapon && !isCustomAmmo) {
            CpRedCustomWeapons weapon = customWeaponRepo.findById(weaponId)
                    .orElseThrow(() -> new EntityNotFoundException("Broń o podanym id nie została znaleziona."));
            CpRedAmmunition ammo = ammoRepo.findById(ammunitionId)
                    .orElseThrow(() -> new EntityNotFoundException("Amunicja o podanym id nie została znaleziona."));

            CpRedCustomWeaponAmmunitionCompatibility entity = new CpRedCustomWeaponAmmunitionCompatibility(weapon, ammo);
            customWeaponAmmoRepo.save(entity);
            return CustomReturnables.getOkResponseMap("Dodano kompatybilność CustomWeapon - Ammo.");

        } else {
            CpRedCustomWeapons weapon = customWeaponRepo.findById(weaponId)
                    .orElseThrow(() -> new EntityNotFoundException("Broń o podanym id nie została znaleziona."));
            CpRedCustomAmmunition ammo = customAmmoRepo.findById(ammunitionId)
                    .orElseThrow(() -> new EntityNotFoundException("Amunicja o podanym id nie została znaleziona."));

            CpRedCustomWeaponCustomAmmunitionCompatibility entity = new CpRedCustomWeaponCustomAmmunitionCompatibility(weapon, ammo);
            customWeaponCustomAmmoRepo.save(entity);
            return CustomReturnables.getOkResponseMap("Dodano kompatybilność CustomWeapon - CustomAmmo.");
        }
    }
}

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

import java.util.List;
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


    public Map<String, Object> allCompatibility() {
        List<CpRedAmmunitionCompatibilityDTO>
    }

    public Map<String, Object> addCompatibility() {

    }
}

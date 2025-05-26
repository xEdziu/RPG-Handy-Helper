package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedAmmunitionCompatibilityRepository extends JpaRepository<CpRedAmmunitionCompatibility, Long> {
    boolean existsByIdAndIsWeaponCustom(Long weaponId, boolean b);

    boolean existsByWeaponIdAndAmmunitionIdAndIsWeaponCustomAndIsAmmunitionCustom(Long weaponId, Long ammunitionId, boolean weaponCustom, boolean ammunitionCustom);
    
    List<CpRedAmmunitionCompatibilityDTO> findAllByWeaponIdAndIsWeaponCustom(Long weaponId, boolean weaponCustom);

    List<CpRedAmmunitionCompatibilityDTO> findAllByAmmunitionIdAndIsAmmunitionCustom(Long ammunitionId, boolean ammunitionCustom);
}

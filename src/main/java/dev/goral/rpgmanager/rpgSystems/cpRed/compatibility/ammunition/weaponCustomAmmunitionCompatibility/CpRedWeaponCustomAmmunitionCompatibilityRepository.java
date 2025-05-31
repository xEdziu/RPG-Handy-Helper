package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition.weaponCustomAmmunitionCompatibility;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedWeaponCustomAmmunitionCompatibilityRepository extends JpaRepository<CpRedWeaponCustomAmmunitionCompatibility, Long> {
}
package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition.weaponAmmunitionCompatibility;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedWeaponAmmunitionCompatibilityRepository extends JpaRepository<CpRedWeaponAmmunitionCompatibility, Long> {
}
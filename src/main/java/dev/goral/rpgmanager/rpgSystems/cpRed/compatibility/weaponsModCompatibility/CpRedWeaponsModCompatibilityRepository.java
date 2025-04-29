package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.weaponsModCompatibility;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedWeaponsModCompatibilityRepository extends JpaRepository<CpRedWeaponsModCompatibility,Long> {
}
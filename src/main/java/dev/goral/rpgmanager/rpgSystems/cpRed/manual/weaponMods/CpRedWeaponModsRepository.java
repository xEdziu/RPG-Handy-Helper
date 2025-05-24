package dev.goral.rpgmanager.rpgSystems.cpRed.manual.weaponMods;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedWeaponModsRepository extends JpaRepository<CpRedWeaponMods,Long> {
}
package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customWeaponMods;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCustomWeaponModsRepository extends JpaRepository<CpRedCustomWeaponMods,Long> {
}
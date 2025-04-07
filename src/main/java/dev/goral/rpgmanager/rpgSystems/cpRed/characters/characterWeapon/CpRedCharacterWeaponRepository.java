package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterWeapon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCharacterWeaponRepository extends JpaRepository<CpRedCharacterWeapon, Long> {
}
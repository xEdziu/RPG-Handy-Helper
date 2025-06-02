package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCharacterCustomWeaponRepository extends JpaRepository<CpRedCharacterCustomWeapon, Long> {
}
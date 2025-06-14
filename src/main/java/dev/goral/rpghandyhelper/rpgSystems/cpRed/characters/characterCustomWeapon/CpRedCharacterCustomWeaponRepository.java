package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon.CpRedCharacterWeapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCharacterCustomWeaponRepository extends JpaRepository<CpRedCharacterCustomWeapon, Long> {
    List<CpRedCharacterCustomWeapon> findAllByCharacter(CpRedCharacters character);
}
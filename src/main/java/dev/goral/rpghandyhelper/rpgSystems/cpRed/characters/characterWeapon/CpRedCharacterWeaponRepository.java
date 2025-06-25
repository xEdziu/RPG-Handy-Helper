package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCharacterWeaponRepository extends JpaRepository<CpRedCharacterWeapon, Long> {
    List<CpRedCharacterWeapon> findAllByCharacter(CpRedCharacters character);
}
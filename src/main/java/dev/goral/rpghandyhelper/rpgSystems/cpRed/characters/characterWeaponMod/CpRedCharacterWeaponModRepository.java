package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeaponMod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCharacterWeaponModRepository extends JpaRepository<CpRedCharacterWeaponMod,Long> {
    List<CpRedCharacterWeaponMod> findAllByCharacterId(Long characterId);

    List<CpRedCharacterWeaponMod> findAllByCharacterWeaponIdAndIsCharacterWeaponCustom(Long characterWeaponId, Boolean isCharacterWeaponCustom);

    boolean existsByCharacterIdAndCharacterWeaponIdAndIsCharacterWeaponCustomAndWeaponModIdAndIsWeaponModCustom(Long characterId, Long characterWeaponId, Boolean isCharacterWeaponCustom, Long weaponModId, Boolean isWeaponModCustom);
}
package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.ammunition.CpRedAmmunitionCompatibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCharacterAmmunitionRepository extends JpaRepository<CpRedCharacterAmmunition, Long> {
    List<CpRedCharacterAmmunition> findAllByCharacterId(Long characterId);

    boolean existsByCharacterIdAndCharacterWeaponIdAndIsCharacterWeaponCustom(Long characterId, Long characterWeaponId, Boolean isCharacterWeaponCustom);

    boolean existsByCharacterIdAndCharacterWeaponIdAndIsCharacterWeaponCustomAndAmmunitionIdAndIsAmmunitionCustom(Long characterId, Long characterWeaponId, Boolean isCharacterWeaponCustom, Long ammunitionId, Boolean isAmmunitionCustom);
}

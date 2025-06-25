package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomAmmunition;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customAmmunition.CpRedCustomAmmunition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCharacterCustomAmmunitionRepository extends JpaRepository<CpRedCharacterCustomAmmunition, Long> {
    List<CpRedCharacterCustomAmmunition> findAllByCharacterId(Long characterId);

    boolean existsByCharacterAndCustomAmmunition(CpRedCharacters character, CpRedCustomAmmunition customAmmunition);

    List<CpRedCharacterCustomAmmunition> findAllByCharacter(CpRedCharacters character);
}

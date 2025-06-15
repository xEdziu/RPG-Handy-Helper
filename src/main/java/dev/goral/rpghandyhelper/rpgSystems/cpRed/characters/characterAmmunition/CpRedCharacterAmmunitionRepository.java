package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition.CpRedAmmunition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterAmmunitionRepository extends JpaRepository<CpRedCharacterAmmunition, Long> {
    List<CpRedCharacterAmmunition> findAllByCharacterId(Long characterId);

    boolean existsByCharacterAndAmmunition(CpRedCharacters character, CpRedAmmunition ammunition);
}

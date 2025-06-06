package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterClasses;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.classes.CpRedClasses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterClassesRepository extends JpaRepository<CpRedCharacterClasses, Long> {

    List<CpRedCharacterClasses> getCharacterClassesByCharacterId(CpRedCharacters characterId);

    boolean existsByCharacterIdAndClassId(CpRedCharacters character, CpRedClasses characterClass);
}

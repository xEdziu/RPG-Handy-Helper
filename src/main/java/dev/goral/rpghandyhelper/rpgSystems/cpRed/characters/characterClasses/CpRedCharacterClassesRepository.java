package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterClasses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterClassesRepository extends JpaRepository<CpRedCharacterClasses, Long> {

    List<CpRedCharacterClasses> getCharacterClassesByCharacterId(Long characterId);
}

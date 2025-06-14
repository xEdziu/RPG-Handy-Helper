package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterLifePaths;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterLifePathsRepository extends JpaRepository<CpRedCharacterLifePaths, Long> {
    List<CpRedCharacterLifePaths> findAllByCharacterId_Id(Long characterId);
    Boolean existsByCharacterId(CpRedCharacters character);
}
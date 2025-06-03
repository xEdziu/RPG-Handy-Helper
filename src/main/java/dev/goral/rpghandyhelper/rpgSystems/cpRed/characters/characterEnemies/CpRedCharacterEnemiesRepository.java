package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEnemies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterEnemiesRepository extends JpaRepository<CpRedCharacterEnemies,Long> {
    List<CpRedCharacterEnemies> findAllByCharacterId_Id(Long characterId);
    Boolean existsByNameAndCharacterId(String name, Long characterId);


}
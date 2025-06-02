package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEnemies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCharacterEnemiesRepository extends JpaRepository<CpRedCharacterEnemies,Long> {
}
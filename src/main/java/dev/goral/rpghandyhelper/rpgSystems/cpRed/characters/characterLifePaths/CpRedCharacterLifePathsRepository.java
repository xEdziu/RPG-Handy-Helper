package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterLifePaths;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCharacterLifePathsRepository extends JpaRepository<CpRedCharacterLifePaths, Long> {
}
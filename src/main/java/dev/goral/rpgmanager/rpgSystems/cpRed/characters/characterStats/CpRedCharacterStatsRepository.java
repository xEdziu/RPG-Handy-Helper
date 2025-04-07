package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterStats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCharacterStatsRepository extends JpaRepository<CpRedCharacterStats, Long> {
}
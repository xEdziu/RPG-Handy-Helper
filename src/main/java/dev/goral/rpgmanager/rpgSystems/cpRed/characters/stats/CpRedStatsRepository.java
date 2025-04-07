package dev.goral.rpgmanager.rpgSystems.cpRed.characters.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedStatsRepository extends JpaRepository<CpRedStats, Long> {
}
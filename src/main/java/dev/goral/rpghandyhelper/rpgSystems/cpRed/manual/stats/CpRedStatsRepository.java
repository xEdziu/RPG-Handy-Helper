package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedStatsRepository extends JpaRepository<CpRedStats, Long> {

    boolean existsByName(String name);
    boolean existsByTag(String tag);
    boolean existsById(Long id);
}
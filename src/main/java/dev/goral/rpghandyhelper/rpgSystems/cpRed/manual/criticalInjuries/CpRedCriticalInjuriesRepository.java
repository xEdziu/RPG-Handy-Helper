package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.criticalInjuries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCriticalInjuriesRepository extends JpaRepository<CpRedCriticalInjuries, Long> {
    Boolean existsByName(String name);
}
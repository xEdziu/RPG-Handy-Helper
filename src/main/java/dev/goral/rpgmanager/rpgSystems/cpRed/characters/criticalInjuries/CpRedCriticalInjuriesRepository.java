package dev.goral.rpgmanager.rpgSystems.cpRed.characters.criticalInjuries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCriticalInjuriesRepository extends JpaRepository<CpRedCriticalInjuries, Long> {
}
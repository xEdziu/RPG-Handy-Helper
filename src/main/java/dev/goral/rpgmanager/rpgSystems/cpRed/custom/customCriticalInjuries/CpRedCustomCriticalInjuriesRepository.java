package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customCriticalInjuries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCustomCriticalInjuriesRepository extends JpaRepository<CpRedCustomCriticalInjuries, Long> {
}
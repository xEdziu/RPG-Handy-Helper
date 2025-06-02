package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.ammunition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedAmmunitionRepository extends JpaRepository<CpRedAmmunition, Long> {

    boolean existsByName(String name);
}
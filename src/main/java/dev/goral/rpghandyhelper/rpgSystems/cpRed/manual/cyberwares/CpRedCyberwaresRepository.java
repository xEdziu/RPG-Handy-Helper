package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.cyberwares;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCyberwaresRepository extends JpaRepository<CpRedCyberwares, Long> {
    boolean existsByName(String name);
}
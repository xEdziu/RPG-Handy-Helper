package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.classes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedClassesRepository extends JpaRepository<CpRedClasses, Long> {
    boolean existsByName(String name);
}
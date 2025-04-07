package dev.goral.rpgmanager.rpgSystems.cpRed.characters.classes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedClassesRepository extends JpaRepository<CpRedClasses, Long> {
}
package dev.goral.rpgmanager.rpgSystems.cpRed.characters.skills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedSkillsRepository extends JpaRepository<CpRedSkills, Long> {
}
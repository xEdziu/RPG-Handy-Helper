package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterSkills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCharacterSkillsRepository extends JpaRepository<CpRedCharacterSkills, Long> {
}
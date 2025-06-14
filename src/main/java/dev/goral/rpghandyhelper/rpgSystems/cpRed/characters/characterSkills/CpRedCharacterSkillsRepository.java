package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterSkills;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.skills.CpRedSkills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterSkillsRepository extends JpaRepository<CpRedCharacterSkills, Long> {
    List<CpRedCharacterSkills> getCharacterSkillsByCharacter(CpRedCharacters character);

    boolean existsByCharacterAndSkill(CpRedCharacters character, CpRedSkills skill);
}
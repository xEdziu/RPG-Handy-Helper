package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterSkills;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterSkillsDTO {
    private int characterId;
    private int skillId;
    private int skillLevel;
}

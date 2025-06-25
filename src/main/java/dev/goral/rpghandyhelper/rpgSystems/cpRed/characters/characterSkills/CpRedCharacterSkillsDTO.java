package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterSkills;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterSkillsDTO {
    private Long id;
    private Long characterId;
    private Long skillId;
    private int skillLevel;
}

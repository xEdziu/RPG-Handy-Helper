package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterSkills;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterSkillsSheetDTO {
    private Long characterSkillId; // Id połączenia umiejętności z postacią
    private Long manualSkillId; // Id umiejętności w podręczniku
    private String nameAndTag; // Sklejona nazwa i tak powiązanej statystyki
    private Integer skillLevel;
    private String category;
}

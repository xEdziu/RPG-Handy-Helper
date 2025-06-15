package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterSkills;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCharacterSkillsRequest {
    private Long skillId;
    private Long characterId;
    private Integer skillLevel;
}

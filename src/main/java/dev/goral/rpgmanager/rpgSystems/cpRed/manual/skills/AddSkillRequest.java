package dev.goral.rpgmanager.rpgSystems.cpRed.manual.skills;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSkillRequest {
    private CpRedSkillsCategory category;
    private String name;
    private Long connectedStatId;
    private String description;
}

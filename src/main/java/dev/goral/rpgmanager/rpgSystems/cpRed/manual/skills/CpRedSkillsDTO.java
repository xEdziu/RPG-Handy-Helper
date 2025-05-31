package dev.goral.rpgmanager.rpgSystems.cpRed.manual.skills;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedSkillsDTO {
    private String category;
    private String name;
    private String connectedStatTag;
    private String description;
}
package dev.goral.rpgmanager.rpgSystems.cpRed.characters.Skills;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedSkillsDTO {
    private String category;
    private String name;
    private int connecectedStatId;
    private String description;
}
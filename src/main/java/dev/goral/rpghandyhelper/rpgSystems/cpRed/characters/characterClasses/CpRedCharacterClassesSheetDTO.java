package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterClassesSheetDTO {
    private Long classId;
    private String className;
    private String SpecialAbility;
    private Short classLevel;
}

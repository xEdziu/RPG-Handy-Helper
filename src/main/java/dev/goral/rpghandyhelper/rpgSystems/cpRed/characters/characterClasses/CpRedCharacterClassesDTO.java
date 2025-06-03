package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterClassesDTO {
    private Short classLevel;
    private Long characterId;
    private Long classId;
}

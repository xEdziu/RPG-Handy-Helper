package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CpRedCharacterClassesDTO {
    private Long id;
    private Short classLevel;
    private Long characterId;
    private Long classId;
}

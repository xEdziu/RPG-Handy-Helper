package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCharacterClassesRequest {
    private short classLevel = -1;
    private Long characterId;
    private Long classId;
}

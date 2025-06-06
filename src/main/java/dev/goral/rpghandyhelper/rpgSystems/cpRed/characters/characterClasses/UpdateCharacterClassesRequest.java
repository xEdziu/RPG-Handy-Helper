package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCharacterClassesRequest {
    private short classLevel = -1;
}

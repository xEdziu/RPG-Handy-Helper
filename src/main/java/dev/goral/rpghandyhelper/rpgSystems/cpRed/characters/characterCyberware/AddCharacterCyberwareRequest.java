package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCyberware;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCharacterCyberwareRequest {
    private Long characterId;
    private Long baseCyberwareId;
}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCyberware;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterCyberwareDTO {
    private Long id;
    private Long baseCyberwareId;
    private Long characterId;
    private String description;
}

package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCyberware;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterCyberwareDTO {
    private long cyberwareId;
    private long characterId;
    private String status;
    private String description;
}

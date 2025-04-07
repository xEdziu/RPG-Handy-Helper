package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCyberware;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterCyberwareDTO {
    private Long cyberwareId;
    private Long characterId;
    private String status;
    private String description;
}

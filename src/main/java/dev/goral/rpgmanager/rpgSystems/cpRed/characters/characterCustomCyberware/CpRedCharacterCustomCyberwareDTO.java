package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomCyberware;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CpRedCharacterCustomCyberwareDTO {
    private long cyberwareId;
    private long characterId;
    private String status;
    private String description;
}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CpRedCharacterCustomCyberwareDTO {
    private Long cyberwareId;
    private Long characterId;
    private String status;
    private String description;
}

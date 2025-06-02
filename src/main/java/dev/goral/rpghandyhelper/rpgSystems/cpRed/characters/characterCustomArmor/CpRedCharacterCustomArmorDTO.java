package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomArmor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterCustomArmorDTO {
    private Long armorId;
    private Long characterId;
    private String status;
    private int currentArmorPoints;
    private String description;
}
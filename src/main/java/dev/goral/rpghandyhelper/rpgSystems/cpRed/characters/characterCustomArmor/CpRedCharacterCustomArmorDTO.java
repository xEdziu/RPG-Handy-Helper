package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomArmor;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor.CpRedCharacterArmorPlace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterCustomArmorDTO {
    private Long id;
    private Long armorId;
    private Long characterId;
    private String status;
    private String place;
    private int currentArmorPoints;
    private String description;
}
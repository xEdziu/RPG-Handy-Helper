package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterArmorDTO {
    private Long armorId;
    private Long characterId;
    private String status;
    private int currentArmorPoints;
}
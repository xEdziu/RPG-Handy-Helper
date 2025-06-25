package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterArmorSheetDTO {
    private Long characterArmorId;
    private Long baseArmorId;
    private Boolean isBaseArmorCustom;
    private String armorName;
    private String place;
    private Integer currentArmorPoints;
    private Integer penalty;
}

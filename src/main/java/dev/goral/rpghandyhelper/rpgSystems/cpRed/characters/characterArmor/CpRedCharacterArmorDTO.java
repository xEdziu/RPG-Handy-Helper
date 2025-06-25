package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterArmorDTO {
    private Long id;
    private Long baseArmorId;
    private Long characterId;
    private String status;
    private String place;
    private Integer currentArmorPoints;
    private String description;
}
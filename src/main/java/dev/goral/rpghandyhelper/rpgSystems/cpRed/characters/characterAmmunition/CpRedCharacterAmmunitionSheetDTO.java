package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCharacterAmmunitionSheetDTO {
    private Long characterAmmunitionId;
    private Long ammunitionId;
    private Boolean isAmmunitionCustom;
    private String ammunitionName;
    private Integer amount;
}

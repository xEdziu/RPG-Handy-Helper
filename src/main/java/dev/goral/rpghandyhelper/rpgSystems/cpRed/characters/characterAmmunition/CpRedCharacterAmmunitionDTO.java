package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCharacterAmmunitionDTO {
    private Long id;
    private Long characterId;
    private Long characterWeaponId;
    private Boolean isCharacterWeaponCustom;
    private Long ammunitionId;
    private Boolean isAmmunitionCustom;
    private Integer amount;
}

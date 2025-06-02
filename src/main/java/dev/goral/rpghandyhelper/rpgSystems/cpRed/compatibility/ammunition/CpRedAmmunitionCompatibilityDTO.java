package dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.ammunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedAmmunitionCompatibilityDTO {
    private Long id;
    private Long weaponId;
    private Long ammunitionId;
    private boolean isWeaponCustom;
    private boolean isAmmunitionCustom;
}

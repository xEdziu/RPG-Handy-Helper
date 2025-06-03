package dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.ammunition;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddAmmunitionCompatibilityRequest {
    private Long weaponId = -1L;
    private Long ammunitionId = -1L;
    private boolean isWeaponCustom;
    private boolean isAmmunitionCustom;
}

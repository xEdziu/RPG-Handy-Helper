package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedAmmunitionCompatibilityRequestDTO {
    private Long weaponId;
    private Long ammoId;
    private boolean isWeaponCustom;
    private boolean isAmmoCustom;
}

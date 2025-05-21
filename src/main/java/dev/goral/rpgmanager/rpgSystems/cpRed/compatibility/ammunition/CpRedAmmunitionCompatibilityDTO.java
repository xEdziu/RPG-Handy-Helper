package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedAmmunitionCompatibilityDTO {
    private Long ammunitionId;
    private Long customAmmunitionId;
    private boolean isCustomWeapon;
    private boolean isCustomAmmunition;
}

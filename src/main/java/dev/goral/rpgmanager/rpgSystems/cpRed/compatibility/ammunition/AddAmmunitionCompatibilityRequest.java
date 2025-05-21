package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddAmmunitionCompatibilityRequest {
    private Long ammunitionId = -1L;
    private Long weaponId = -1L;
    private boolean isCustomWeapon = false;
    private boolean isCustomAmmunition = false;
}

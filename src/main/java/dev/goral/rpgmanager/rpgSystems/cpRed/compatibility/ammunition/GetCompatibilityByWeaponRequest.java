package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.ammunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCompatibilityByWeaponRequest {
    private Long weaponId;
    private boolean isWeaponCustom;
}

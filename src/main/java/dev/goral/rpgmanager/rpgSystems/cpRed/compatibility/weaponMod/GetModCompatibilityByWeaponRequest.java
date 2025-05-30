package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.weaponMod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetModCompatibilityByWeaponRequest {
    private Long weaponId;
    private boolean isWeaponCustom;
}

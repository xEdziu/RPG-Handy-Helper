package dev.goral.rpgmanager.rpgSystems.cpRed.compatibility.weaponMod;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddModCompatibilityRequest {
    private Long weaponId = -1L;
    private Long modId = -1L;
    private boolean isWeaponCustom;
    private boolean isModCustom;
}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.compatibility.weaponMod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedModCompatibilityDTO {
    private Long id;
    private Long weaponId;
    private Long modId;
    private boolean isWeaponCustom;
    private boolean isModCustom;
}

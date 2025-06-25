package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeaponMod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetModsForWeaponRequest {
    private Long characterWeaponId;
    private Boolean isCharacterWeaponCustom;
}

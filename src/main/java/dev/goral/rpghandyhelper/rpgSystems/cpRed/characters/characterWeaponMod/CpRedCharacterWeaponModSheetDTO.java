package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeaponMod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCharacterWeaponModSheetDTO {
    private Long characterWeaponModId;
    private Long weaponModId;
    private Boolean isWeaponModCustom;
    private String weaponModName;
}

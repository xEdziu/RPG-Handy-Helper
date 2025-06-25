package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeaponMod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCharacterWeaponModDTO {
    private Long id;
    private Long characterId;
    private Long characterWeaponId;
    private Boolean isCharacterWeaponCustom;
    private Long weaponModId;
    private Boolean isWeaponModCustom;
    private Integer sizeTaken;
}
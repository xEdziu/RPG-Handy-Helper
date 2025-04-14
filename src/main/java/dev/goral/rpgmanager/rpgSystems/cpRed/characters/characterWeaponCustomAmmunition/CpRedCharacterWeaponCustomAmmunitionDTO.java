package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterWeaponCustomAmmunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCharacterWeaponCustomAmmunitionDTO {
    private Long characterWeaponId;
    private Long customAmmunitionId;
    private int amount;
}

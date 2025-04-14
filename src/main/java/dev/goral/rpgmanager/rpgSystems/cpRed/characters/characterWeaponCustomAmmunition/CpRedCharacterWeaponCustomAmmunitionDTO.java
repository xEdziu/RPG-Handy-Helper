package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterWeaponCustomAmmunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCharacterWeaponCustomAmmunitionDTO {
    private int characterWeaponId;
    private int customAmmunitionId;
    private int amount;
}

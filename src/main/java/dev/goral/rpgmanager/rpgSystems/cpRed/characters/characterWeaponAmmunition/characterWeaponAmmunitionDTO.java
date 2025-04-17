package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterWeaponAmmunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class characterWeaponAmmunitionDTO {
    private Long characterWeaponId;
    private Long ammunitionId;
    private int amount;
}

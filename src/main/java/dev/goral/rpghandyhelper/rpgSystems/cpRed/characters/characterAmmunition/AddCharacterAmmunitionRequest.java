package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCharacterAmmunitionRequest {
    private Long characterId;
    private Long characterWeaponId;
    private Boolean isCharacterWeaponCustom;
    private Long ammunitionId;
    private Boolean isAmmunitionCustom;
    private Integer amount;
}

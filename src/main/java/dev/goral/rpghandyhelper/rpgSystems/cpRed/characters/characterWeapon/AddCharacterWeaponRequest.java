package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterWeapon;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCharacterWeaponRequest {
    private Long baseWeaponId;
    private Long characterId;
    private CpRedCharacterItemStatus status;
}

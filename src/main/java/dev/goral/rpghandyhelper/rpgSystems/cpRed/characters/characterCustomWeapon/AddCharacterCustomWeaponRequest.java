package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomWeapon;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCharacterCustomWeaponRequest {
    private Long baseCustomWeaponId;
    private Long characterId;
    private CpRedCharacterItemStatus status;
}

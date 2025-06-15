package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomArmor;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCharacterCustomArmorRequest {
    private Long armorId;
    private Long characterId;
    private CpRedCharacterItemStatus status;
}

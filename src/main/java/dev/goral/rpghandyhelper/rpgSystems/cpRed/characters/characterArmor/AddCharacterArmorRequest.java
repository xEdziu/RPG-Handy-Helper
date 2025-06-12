package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCharacterArmorRequest {
    private Long baseArmorId;
    private Long characterId;
    private CpRedCharacterItemStatus status;
}

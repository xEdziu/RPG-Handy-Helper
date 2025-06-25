package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEquipment;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCharacterEquipmentRequest {
    private Long itemId;
    private Long characterId;
    private Integer quantity;
    private CpRedCharacterItemStatus status;
}

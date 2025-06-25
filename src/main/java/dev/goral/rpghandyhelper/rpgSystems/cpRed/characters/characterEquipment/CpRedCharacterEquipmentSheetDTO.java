package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEquipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterEquipmentSheetDTO {
    private Long characterItemId;
    private Long itemId;
    private Boolean isItemCustom;
    private String itemName;
    private Integer itemQuantity;
    private String itemDescription;
    private String itemStatus;
}

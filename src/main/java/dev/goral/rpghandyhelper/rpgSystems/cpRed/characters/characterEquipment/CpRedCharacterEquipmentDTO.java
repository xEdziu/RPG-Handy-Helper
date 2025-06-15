package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEquipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterEquipmentDTO {
    private Long id;
    private Long characterId;
    private Long itemId;
    private Integer quantity;
    private String status;
    private String description;
}
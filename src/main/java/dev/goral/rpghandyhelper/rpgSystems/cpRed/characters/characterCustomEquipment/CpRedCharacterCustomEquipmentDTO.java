package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomEquipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CpRedCharacterCustomEquipmentDTO {
    private Long id;
    private Long characterId;
    private Long customItemId;
    private Integer quantity;
    private String status;
    private String description;
}
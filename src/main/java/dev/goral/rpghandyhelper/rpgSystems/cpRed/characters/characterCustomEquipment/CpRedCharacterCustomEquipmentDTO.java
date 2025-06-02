package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomEquipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CpRedCharacterCustomEquipmentDTO {
    private Long equipmentId;
    private Long characterId;
    private String status;
    private String description;
}
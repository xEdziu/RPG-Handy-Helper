package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomEquipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CpRedCharacterCustomEquipmentDTO {
    private long equipmentId;
    private long characterId;
    private String status;
    private String description;
}
package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterEquipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterEquipmentDTO {
    private long characterId;
    private long itemId;
    private String availability;
    private String description;
}
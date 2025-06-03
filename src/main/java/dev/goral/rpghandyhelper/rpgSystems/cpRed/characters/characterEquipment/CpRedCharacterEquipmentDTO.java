package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEquipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterEquipmentDTO {
    private Long characterId;
    private Long itemId;
    private String availability;
    private String description;
}
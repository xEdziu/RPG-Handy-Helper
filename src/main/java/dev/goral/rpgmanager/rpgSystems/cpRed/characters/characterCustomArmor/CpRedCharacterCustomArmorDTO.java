package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomArmor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterCustomArmorDTO {
    private Long armorId;
    private Long characterId;
    private String status;
    private String description;
}
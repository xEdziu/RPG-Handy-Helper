package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomArmor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterCustomArmorDTO {
    private long armorId;
    private long characterId;
    private String status;
    private String description;
}
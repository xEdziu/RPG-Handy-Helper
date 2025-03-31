package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterArmor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterArmorDTO {
    private long armorId;
    private long characterId;
    private String status;
    private String description;
}
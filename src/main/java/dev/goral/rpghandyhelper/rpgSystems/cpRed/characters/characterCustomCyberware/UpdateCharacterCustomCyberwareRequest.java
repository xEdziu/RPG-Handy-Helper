package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCharacterCustomCyberwareRequest {
    private CpRedCharacterItemStatus status;
    private String description;
}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCharacterCustomCyberwareRequest {
    private Long cyberwareId;
    private Long characterId;
}

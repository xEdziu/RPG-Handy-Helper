package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomAmmunition;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCharacterCustomAmmunitionRequest {
    private Long characterId;
    private Long customAmmunitionId;
    private Integer amount;
    private CpRedCharacterItemStatus status;
}

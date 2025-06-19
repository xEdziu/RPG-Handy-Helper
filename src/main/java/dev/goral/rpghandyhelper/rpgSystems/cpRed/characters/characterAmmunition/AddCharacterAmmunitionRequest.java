package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterAmmunition;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCharacterAmmunitionRequest {
    private Long characterId;
    private Long ammunitionId;
    private Integer amount;
    private CpRedCharacterItemStatus status;
}

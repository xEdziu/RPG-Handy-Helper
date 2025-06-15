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
public class UpdateCharacterAmmunitionRequest {
    private CpRedCharacterItemStatus status;
    private Integer amount;
}

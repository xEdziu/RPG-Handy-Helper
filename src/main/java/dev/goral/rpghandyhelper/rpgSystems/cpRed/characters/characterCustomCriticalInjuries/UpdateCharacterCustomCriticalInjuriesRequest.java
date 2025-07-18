package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries.CpRedCharacterCriticalInjuriesStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCharacterCustomCriticalInjuriesRequest {
    private CpRedCharacterCriticalInjuriesStatus status;
}

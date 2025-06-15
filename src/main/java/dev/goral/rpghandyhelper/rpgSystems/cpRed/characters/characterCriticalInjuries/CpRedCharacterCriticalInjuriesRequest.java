package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCharacterCriticalInjuriesRequest {
    private Long characterId;
    private Long injuriesId;
    private CpRedCharacterCriticalInjuriesStatus status;
}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCharacterCriticalInjuriesDTO {
    private String status;
    private Long characterId;
    private Long injuriesId;
}
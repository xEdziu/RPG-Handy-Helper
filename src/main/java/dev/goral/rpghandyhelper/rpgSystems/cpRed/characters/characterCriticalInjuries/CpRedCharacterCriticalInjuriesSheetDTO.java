package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCharacterCriticalInjuriesSheetDTO {
    private Long characterCriticalInjuriesId;
    private Long baseCriticalInjuriesId;
    private Boolean isCriticalInjuriesCustom;
    private String criticalInjuriesName;
    private String criticalInjuriesStatus;
}

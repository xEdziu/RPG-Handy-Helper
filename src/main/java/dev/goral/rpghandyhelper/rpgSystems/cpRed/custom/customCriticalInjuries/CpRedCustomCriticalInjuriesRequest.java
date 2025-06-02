package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCriticalInjuries;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.criticalInjuries.CpRedCriticalInjuriesInjuryPlace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCustomCriticalInjuriesRequest {
    private Long gameId;
    private CpRedCriticalInjuriesInjuryPlace injuryPlace;
    private String name;
    private String effects;
    private String patching;
    private String treating;
}

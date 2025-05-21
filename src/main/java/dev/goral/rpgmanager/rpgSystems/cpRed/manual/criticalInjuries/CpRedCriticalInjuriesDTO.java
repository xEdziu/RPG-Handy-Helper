package dev.goral.rpgmanager.rpgSystems.cpRed.manual.criticalInjuries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCriticalInjuriesDTO {
    private int rollValue;
    private String injuryPlace;
    private String name;
    private String effects;
    private String patching;
    private String treating;
}

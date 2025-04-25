package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customCriticalInjuries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCustomCriticalInjuriesDTO {
    private Long gameId;
    private String injuryPlace;
    private String name;
    private String effects;
    private String patching;
    private String treating;
}

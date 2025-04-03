package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterOtherInfo;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterOtherInfoDTO {
    private long characterId;
    private String notes;
    private String criticalInjuries;
    private String addictions;
    private String reputation;
    private String style;
    private String classLifePath;
    private String accomodation;
    private int rental;
    private String livingStandard;
}

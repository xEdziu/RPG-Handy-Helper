package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterOtherInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCharacterOtherInfoRequest {
    private Long characterId;
    private String notes;
    private String addictions;
    private String reputation;
    private String style;
    private String classLifePath;
    private String accommodation;
    private int rental;
    private String livingStandard;
}

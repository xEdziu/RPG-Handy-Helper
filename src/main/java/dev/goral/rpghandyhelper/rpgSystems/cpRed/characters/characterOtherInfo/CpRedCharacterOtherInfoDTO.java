package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterOtherInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterOtherInfoDTO {
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
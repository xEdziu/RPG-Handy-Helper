package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCyberware;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterCyberwareSheetDTO {
    private Long characterCyberwareId;
    private Long cyberwareId;
    private Boolean isCyberwareCustom;
    private String cyberwareName;
    private String cyberwareDescription;
    private String mountPlace;
}

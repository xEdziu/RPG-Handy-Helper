package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEnemies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterEnemiesSheetDTO {
    private Long characterEnemiesId;
    private String enemiesName;
    private String whoIs;
    private String causeOfConflict;
    private String whatHas;
    private String intends;
    private String enemiesDescription;
}

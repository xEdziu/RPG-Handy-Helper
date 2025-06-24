package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterStats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterStatsSheetDTO {
    private Long characterStatsId; // Id połączenia statystyki z postacią
    private Long manualStatId; // Id statystyki w podręczniku
    private String Tag;
    private Integer maxStatLevel;
    private Integer currentStatLevel;
}

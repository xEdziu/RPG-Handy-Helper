package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterStats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterStatsDTO {
    private Long id;
    private Integer maxStatLevel;
    private Integer currentStatLevel;
    private Long characterId;
    private Long statId;
}
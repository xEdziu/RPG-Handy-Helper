package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterStats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterStatsDTO {
    private int statLevelMax;
    private int statLevelActual;
    private Long characterId;
    private Long statId;
}
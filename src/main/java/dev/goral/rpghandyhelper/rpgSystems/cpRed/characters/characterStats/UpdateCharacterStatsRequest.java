package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterStats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCharacterStatsRequest {
    private Integer maxStatLevel;
    private Integer currentStatLevel;
}

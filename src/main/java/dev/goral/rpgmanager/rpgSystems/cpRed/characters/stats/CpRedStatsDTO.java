package dev.goral.rpgmanager.rpgSystems.cpRed.characters.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedStatsDTO {
    private Long id;
    private String name;
    private String description;
}
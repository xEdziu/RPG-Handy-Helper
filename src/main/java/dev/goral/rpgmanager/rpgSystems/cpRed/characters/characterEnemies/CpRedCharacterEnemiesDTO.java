package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterEnemies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterEnemiesDTO {
    private Long characterId;
    private String name;
    private String whoIs;
    private String causeOfConflict;
    private String whatHas;
    private String intends;
    private String description;
}
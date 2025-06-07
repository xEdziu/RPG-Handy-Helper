package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEnemies;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCharacterEnemiesRequest {
    private Long characterId;
    private String name;
    private String whoIs;
    private String causeOfConflict;
    private String whatHas;
    private String intends;
    private String description;
}
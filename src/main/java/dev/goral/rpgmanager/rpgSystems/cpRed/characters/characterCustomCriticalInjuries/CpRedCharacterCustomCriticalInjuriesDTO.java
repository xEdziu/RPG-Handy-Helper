package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomCriticalInjuries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCharacterCustomCriticalInjuriesDTO {
    private String status;
    private Long characterId;
    private Long customCriticalInjuriesId;
}
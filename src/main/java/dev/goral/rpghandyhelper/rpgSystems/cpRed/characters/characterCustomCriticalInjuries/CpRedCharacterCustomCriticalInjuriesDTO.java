package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCharacterCustomCriticalInjuriesDTO {
    private Long id;
    private Long characterId;
    private Long customCriticalInjuriesId;
    private String status;
}
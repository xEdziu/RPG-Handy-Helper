package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomAmmunition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class CpRedCharacterCustomAmmunitionDTO {
    private Long id;
    private Long characterId;
    private Long customAmmunitionId;
    private String status;
    private Integer amount;
}

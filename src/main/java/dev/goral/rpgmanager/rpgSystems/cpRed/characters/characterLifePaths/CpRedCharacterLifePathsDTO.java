package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterLifePaths;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.CpRedCharacters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharacterLifePathsDTO {
    private Long characterId;
    private String cultureOfOrigin;
    private String yourCharacter;
    private String clothingAndStyle;
    private String hair;
    private String mostValue;
    private String relationships;
    private String mostImportantPerson;
    private String mostImportantItem;
    private String familyBackground;
    private String familyEnvironment;
    private String familyCrisis;
    private String lifeGoals;
}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterLifePaths;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCharacterLifePathsRequest {
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

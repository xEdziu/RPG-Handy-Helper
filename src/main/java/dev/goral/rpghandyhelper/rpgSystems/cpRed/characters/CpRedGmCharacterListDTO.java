package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedGmCharacterListDTO {
    private Long characterId;
    private String characterName;
    private String characterType;
    private Long userId;
}

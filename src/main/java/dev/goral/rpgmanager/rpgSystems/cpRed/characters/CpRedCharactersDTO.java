package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class CpRedCharactersDTO {
    private final Long id;
    private final Long gameId;
    private final Long userId;
    private final String name;
    private final String nickname;
    private final String type;
    private final Integer expAll;
    private final Integer expAvailable;
    private final Integer cash;
}

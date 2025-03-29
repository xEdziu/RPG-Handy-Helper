package dev.goral.rpgmanager.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class GameDTOAdmin {

    private final Long id;
    private final String name;
    private final String description;
    private final Long gameMasterId;
    private final Long rpgSystemId;
    private final String status;
}

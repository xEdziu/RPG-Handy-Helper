package dev.goral.rpghandyhelper.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class GameDTO {

    private final Long id;
    private final String name;
    private final String description;
    private final Long ownerId;
    private final Long rpgSystemId;

}
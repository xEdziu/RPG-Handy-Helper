package dev.goral.rpghandyhelper.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class UserGamesDTO {
    private Long id;
    private Long rpgSystemId;
    private String name;
    private String description;
}
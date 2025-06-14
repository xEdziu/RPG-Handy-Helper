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
    private String rpgSystemName;
    private String name;
    private String description;
}
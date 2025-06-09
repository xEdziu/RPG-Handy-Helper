package dev.goral.rpghandyhelper.game.gameUsers;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SimpleUserInGameDTO {
    Long id;
    String username;
    String name;
    GameUsersRole role;
    String avatarUrl;
}

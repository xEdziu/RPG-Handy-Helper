package dev.goral.rpghandyhelper.game.gameUsers;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameUsersDTO {
    private Long id;
    private Long userId;
    private String username;
    private Long gameId;
    private String role;
}
package dev.goral.rpgmanager.game.gameUsers;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GameUsersDTO {
    private Long id;
    private Long userId;
    private Long gameId;
    private String role;
}

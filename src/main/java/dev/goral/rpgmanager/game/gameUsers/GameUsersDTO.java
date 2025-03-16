package dev.goral.rpgmanager.game.gameUsers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameUsersDTO {
    private Long id;
    private Long userId;
    private Long gameId;
    private String role;
}

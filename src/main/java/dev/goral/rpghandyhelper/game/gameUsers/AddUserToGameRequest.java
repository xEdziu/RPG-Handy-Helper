package dev.goral.rpghandyhelper.game.gameUsers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserToGameRequest {
    private Long userId;
    private Long gameId;
    private String role;
}
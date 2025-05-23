package dev.goral.rpgmanager.game.gameUsers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteUserFromGameRequest {
    private Long userId;
    private Long gameId;
}

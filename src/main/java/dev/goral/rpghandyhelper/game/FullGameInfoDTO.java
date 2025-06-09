package dev.goral.rpghandyhelper.game;

import dev.goral.rpghandyhelper.game.gameUsers.SimpleUserInGameDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FullGameInfoDTO {
    Long id;
    String name;
    String description;
    String systemName;
    List<SimpleUserInGameDTO> users;
}

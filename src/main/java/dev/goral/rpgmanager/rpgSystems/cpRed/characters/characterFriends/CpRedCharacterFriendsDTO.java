package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterFriends;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CpRedCharacterFriendsDTO {
    private long characterId;
    private String name;
    private String description;
}

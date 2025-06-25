package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterFriends;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CpRedCharacterFriendsSheetDTO {
    private Long characterFriendsId;
    private String friendName;
    private String friendDescription;
}

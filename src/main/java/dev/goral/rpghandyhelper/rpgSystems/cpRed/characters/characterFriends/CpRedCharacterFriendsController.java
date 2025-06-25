package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterFriends;


import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterFriends.CpRedCharacterFriendsRequest;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterFriends.CpRedCharacterFriendsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/authorized")
@AllArgsConstructor
public class CpRedCharacterFriendsController {
    private final CpRedCharacterFriendsService cpRedCharacterFriendsService;

    @GetMapping(path = "/rpgSystems/cpRed/character/friends/all")
    public Map<String, Object> getAllFriends() {
        return cpRedCharacterFriendsService.getAllFriends();
    }

    @GetMapping(path = "/rpgSystems/cpRed/character/friends/{friendId}")
    public Map<String, Object> getFriendById(@PathVariable("friendId") Long friendId) {
        return cpRedCharacterFriendsService.getFriendById(friendId);
    }

    @GetMapping(path = "/rpgSystems/cpRed/character/friends/character/{characterId}")
    public Map<String, Object> getFriendsByCharacterId(@PathVariable("characterId") Long characterId) {
        return cpRedCharacterFriendsService.getFriendsByCharacterId(characterId);
    }

    @GetMapping(path = "/admin/rpgSystems/cpRed/character/friends/all")
    public Map<String, Object> getAllFriendsForAdmin() {
        return cpRedCharacterFriendsService.getAllFriendsForAdmin();
    }

    @PostMapping(path = "/rpgSystems/cpRed/character/friends/add")
    public Map<String, Object> addFriend(@RequestBody CpRedCharacterFriendsRequest cpRedCharacterFriends) {
        return cpRedCharacterFriendsService.addFriend(cpRedCharacterFriends);
    }

    @DeleteMapping(path = "/rpgSystems/cpRed/character/friends/delete/{friendId}")
    public Map<String, Object> deleteFriend(@PathVariable("friendId") Long friendId) {
        return cpRedCharacterFriendsService.deleteFriend(friendId);
    }

    @PutMapping(path = "/rpgSystems/cpRed/character/friends/update/{friendId}")
    public Map<String, Object> updateFriend(@PathVariable("friendId") Long friendId,
                                           @RequestBody CpRedCharacterFriendsRequest cpRedCharacterFriends) {
        return cpRedCharacterFriendsService.updateFriends(friendId, cpRedCharacterFriends);
    }
}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterFriends;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterTragicLoveStory.CpRedCharacterTragicLoveStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCharacterFriendsRepository extends JpaRepository<CpRedCharacterFriends, Long> {
    List<CpRedCharacterFriends> findAllByCharacterId_Id(Long characterId);
    Boolean existsByNameAndCharacterId(String name, CpRedCharacters characterId);

    List<CpRedCharacterFriends> findAllByCharacterId(CpRedCharacters character);
}
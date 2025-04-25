package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterFriends;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCharacterFriendsRepository extends JpaRepository<CpRedCharacterFriends, Long> {
}
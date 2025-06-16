package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterFriends.CpRedCharacterFriends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterCriticalInjuriesRepository extends JpaRepository<CpRedCharacterCriticalInjuries, Long> {
    List<CpRedCharacterCriticalInjuries> findAllByCharacterId(CpRedCharacters character);
    Boolean existsByInjuriesId_IdAndCharacterId(Long injuriesId, CpRedCharacters character);
}
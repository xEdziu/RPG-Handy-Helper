package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterTragicLoveStory;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterTragicLoveStoryRepository extends JpaRepository<CpRedCharacterTragicLoveStory, Long> {
    List<CpRedCharacterTragicLoveStory> findAllByCharacterId_Id(Long characterId);
    Boolean existsByNameAndCharacterId(String name, CpRedCharacters characterId);
}
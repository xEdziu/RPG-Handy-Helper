package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCyberware;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterCustomCyberwareRepository extends JpaRepository<CpRedCharacterCustomCyberware, Long> {
    List<CpRedCharacterCustomCyberware> findAllByCharacterId(CpRedCharacters character);
}
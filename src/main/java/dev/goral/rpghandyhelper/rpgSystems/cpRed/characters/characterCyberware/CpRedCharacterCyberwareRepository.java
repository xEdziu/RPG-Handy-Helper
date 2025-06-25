package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCyberware;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCharacterCyberwareRepository extends JpaRepository<CpRedCharacterCyberware, Long> {
    List<CpRedCharacterCyberware> findByCharacter(CpRedCharacters character);

    List<CpRedCharacterCyberware> findAllByCharacter(CpRedCharacters character);
}
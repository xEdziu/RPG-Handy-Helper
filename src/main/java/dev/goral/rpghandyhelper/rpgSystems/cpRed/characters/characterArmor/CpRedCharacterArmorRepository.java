package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCharacterArmorRepository extends JpaRepository<CpRedCharacterArmor, Long> {
    List<CpRedCharacterArmor> findAllByCharacter(CpRedCharacters character);

    List<CpRedCharacterArmor> findAllByCharacterAndPlace(CpRedCharacters character, CpRedCharacterArmorPlace place);

    List<CpRedCharacterArmor> findAllByCharacterAndStatus(CpRedCharacters character, CpRedCharacterItemStatus status);
}

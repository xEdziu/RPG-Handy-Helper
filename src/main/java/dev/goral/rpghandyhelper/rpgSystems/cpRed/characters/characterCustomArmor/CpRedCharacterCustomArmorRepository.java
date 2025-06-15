package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomArmor;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor.CpRedCharacterArmor;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterArmor.CpRedCharacterArmorPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterCustomArmorRepository extends JpaRepository<CpRedCharacterCustomArmor, Long> {
    List<CpRedCharacterCustomArmor> findAllByCharacterId(CpRedCharacters character);
    List<CpRedCharacterCustomArmor> findAllByCharacterAndPlace(CpRedCharacters character, CpRedCharacterArmorPlace place);

}

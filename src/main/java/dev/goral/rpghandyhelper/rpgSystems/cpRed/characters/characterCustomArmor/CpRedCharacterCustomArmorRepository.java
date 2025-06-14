package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomArmor;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterCustomArmorRepository extends JpaRepository<CpRedCharacterCustomArmor, Long> {
    List<CpRedCharacterCustomArmor> findAllByCharacter(CpRedCharacters character);
}

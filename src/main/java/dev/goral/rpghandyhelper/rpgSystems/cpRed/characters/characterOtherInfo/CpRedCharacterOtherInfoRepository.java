package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterOtherInfo;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterOtherInfoRepository extends JpaRepository<CpRedCharacterOtherInfo, Long> {
    List<CpRedCharacterOtherInfo> findAllByCharacterId_Id(Long characterId);
    Boolean existsByCharacterId(CpRedCharacters character);
}
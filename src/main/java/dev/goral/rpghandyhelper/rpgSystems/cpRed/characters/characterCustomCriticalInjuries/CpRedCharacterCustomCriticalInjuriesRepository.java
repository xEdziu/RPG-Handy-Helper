package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCriticalInjuries.CpRedCustomCriticalInjuries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCharacterCustomCriticalInjuriesRepository extends JpaRepository<CpRedCharacterCustomCriticalInjuries, Long> {
    List<CpRedCharacterCustomCriticalInjuries> findAllByCharacterId_Id(Long characterId);

    boolean existsByCharacterAndCustomInjuries(CpRedCharacters character, CpRedCustomCriticalInjuries customInjuries);
}
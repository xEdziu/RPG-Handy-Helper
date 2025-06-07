package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterStats;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.stats.CpRedStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCharacterStatsRepository extends JpaRepository<CpRedCharacterStats, Long> {
    List<CpRedCharacterStats> getCharacterStatsByCharacterId(CpRedCharacters character);

    boolean existsByCharacterIdAndStatId(CpRedCharacters character, CpRedStats stat);
}
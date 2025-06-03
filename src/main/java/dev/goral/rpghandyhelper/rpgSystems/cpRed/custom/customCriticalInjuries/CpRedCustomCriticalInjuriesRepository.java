package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCriticalInjuries;

import dev.goral.rpghandyhelper.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCustomCriticalInjuriesRepository extends JpaRepository<CpRedCustomCriticalInjuries, Long> {
    List<CpRedCustomCriticalInjuries> findAllByGameId(Game game);
    Boolean existsByNameAndGameId(String name, Game game);
}
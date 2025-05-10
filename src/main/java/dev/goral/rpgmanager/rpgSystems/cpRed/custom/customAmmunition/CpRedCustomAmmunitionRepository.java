package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customAmmunition;

import dev.goral.rpgmanager.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCustomAmmunitionRepository extends JpaRepository<CpRedCustomAmmunition, Long> {
    boolean existsByNameAndGameId(String name, Game game);

    List<CpRedCustomAmmunition> findAllByGameId(Game game);
}

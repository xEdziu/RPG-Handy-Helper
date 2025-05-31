package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customCyberwares;

import dev.goral.rpgmanager.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCustomCyberwaresRepository extends JpaRepository<CpRedCustomCyberwares, Long> {
    boolean existsByNameAndGameId(String name, Game game);
    List<CpRedCustomCyberwares> findAllByGameId(Game game);
}
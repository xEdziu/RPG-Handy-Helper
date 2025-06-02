package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCyberwares;

import dev.goral.rpghandyhelper.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCustomCyberwaresRepository extends JpaRepository<CpRedCustomCyberwares, Long> {
    boolean existsByNameAndGameId(String name, Game game);
    List<CpRedCustomCyberwares> findAllByGameId(Game game);

}
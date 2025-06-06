package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customArmors;

import dev.goral.rpghandyhelper.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCustomArmorsRepository extends JpaRepository<CpRedCustomArmors, Long> {
    List<CpRedCustomArmors> findAllByGameId(Game game);
    Boolean existsByNameAndGameId(String name, Game game);

}
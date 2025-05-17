package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customArmors;

import dev.goral.rpgmanager.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCustomArmorsRepository extends JpaRepository<CpRedCustomArmors, Long> {
    List<CpRedCustomArmors> findAllByGameId(Long gameId);
    Boolean existsByNameAndGameId(String name, Game game);

}
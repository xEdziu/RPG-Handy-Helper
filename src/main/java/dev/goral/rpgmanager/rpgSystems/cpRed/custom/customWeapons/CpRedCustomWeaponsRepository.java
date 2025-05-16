package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeapons;

import dev.goral.rpgmanager.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCustomWeaponsRepository extends JpaRepository<CpRedCustomWeapons, Long> {
    boolean existsByNameAndGameId(String name, Game game);
}
package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeapons;

import dev.goral.rpgmanager.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCustomWeaponsRepository extends JpaRepository<CpRedCustomWeapons, Long> {
    boolean existsByNameAndGameId(String name, Game game);

    List<CpRedCustomWeapons> findAllByGameId_Id(Long gameId);
}
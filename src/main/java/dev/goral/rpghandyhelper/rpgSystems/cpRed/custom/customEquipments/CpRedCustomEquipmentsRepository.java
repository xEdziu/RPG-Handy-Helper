package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customEquipments;

import dev.goral.rpghandyhelper.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCustomEquipmentsRepository extends JpaRepository<CpRedCustomEquipments, Long> {
    List<CpRedCustomEquipments>findAllByGameId(Game game);
    Boolean existsByNameAndGameId(String name, Game game);
}
package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeaponMods;

import dev.goral.rpgmanager.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCustomWeaponModsRepository extends JpaRepository<CpRedCustomWeaponMods,Long> {
    List<CpRedCustomWeaponMods> findAllByGameId(Game game);
    boolean existsByNameAndGameId(String name, Game game);
}
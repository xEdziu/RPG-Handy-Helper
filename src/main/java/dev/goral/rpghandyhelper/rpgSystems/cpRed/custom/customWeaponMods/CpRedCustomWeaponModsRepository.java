package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeaponMods;

import dev.goral.rpghandyhelper.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpRedCustomWeaponModsRepository extends JpaRepository<CpRedCustomWeaponMods,Long> {
    List<CpRedCustomWeaponMods> findAllByGameId(Game game);
    boolean existsByNameAndGameId(String name, Game game);
}
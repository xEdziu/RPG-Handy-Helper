package dev.goral.rpgmanager.rpgSystems.cpRed.characters.weapons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedWeaponsRepository extends JpaRepository<CpRedWeapons, Long> {
}
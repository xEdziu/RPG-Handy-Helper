package dev.goral.rpgmanager.rpgSystems.cpRed.manual.weapons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedWeaponsRepository extends JpaRepository<CpRedWeapons, Long> {
}
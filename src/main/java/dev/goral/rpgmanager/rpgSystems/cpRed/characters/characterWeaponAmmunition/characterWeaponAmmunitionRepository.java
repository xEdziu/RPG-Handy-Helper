package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterWeaponAmmunition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface characterWeaponAmmunitionRepository extends JpaRepository<characterWeaponAmmunition, Long> {
}
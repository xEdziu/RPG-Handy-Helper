package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterArmor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCharacterArmorRepository extends JpaRepository<CpRedCharacterArmor, Long> {
}

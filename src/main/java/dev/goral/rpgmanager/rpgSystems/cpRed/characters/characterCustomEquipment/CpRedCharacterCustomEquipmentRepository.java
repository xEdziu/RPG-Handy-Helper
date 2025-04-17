package dev.goral.rpgmanager.rpgSystems.cpRed.characters.characterCustomEquipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCharacterCustomEquipmentRepository extends JpaRepository<CpRedCharacterCustomEquipment, Long> {
}
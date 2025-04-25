package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customEquipments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCustomEquipmentsRepository extends JpaRepository<CpRedCustomEquipments, Long> {
}
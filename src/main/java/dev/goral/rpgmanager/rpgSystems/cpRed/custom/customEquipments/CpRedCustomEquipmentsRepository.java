package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customEquipments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCustomEquipmentsRepository extends JpaRepository<CpRedCustomEquipments, Long> {
}
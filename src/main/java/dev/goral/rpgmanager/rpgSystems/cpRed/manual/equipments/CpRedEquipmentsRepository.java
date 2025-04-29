package dev.goral.rpgmanager.rpgSystems.cpRed.manual.equipments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedEquipmentsRepository extends JpaRepository<CpRedEquipments, Long> {
}
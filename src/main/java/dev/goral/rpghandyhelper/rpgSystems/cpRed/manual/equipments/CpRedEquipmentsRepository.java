package dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.equipments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedEquipmentsRepository extends JpaRepository<CpRedEquipments, Long> {
    boolean existsByName(String name);
}
package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customArmors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpRedCustomArmorsRepository extends JpaRepository<CpRedCustomArmors, Long> {
}
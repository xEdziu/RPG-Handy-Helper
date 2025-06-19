package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomEquipment;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customEquipments.CpRedCustomEquipments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCharacterCustomEquipmentRepository extends JpaRepository<CpRedCharacterCustomEquipment, Long> {
    List<CpRedCharacterCustomEquipment> findAllByCharacter(CpRedCharacters character);

    boolean existsByCharacterAndCustomItem(CpRedCharacters character, CpRedCustomEquipments customItem);
}
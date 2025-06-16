package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEquipment;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.equipments.CpRedEquipments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CpRedCharacterEquipmentRepository extends JpaRepository<CpRedCharacterEquipment,Long> {
    List<CpRedCharacterEquipment> findAllByCharacter(CpRedCharacters character);

    boolean existsByCharacterAndItem(CpRedCharacters character, CpRedEquipments item);
}
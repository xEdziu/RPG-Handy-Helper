package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomEquipment;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCharacterCustomEquipmentRequest {
    private Integer quantity;
    private CpRedCharacterItemStatus status;
    @Column(length = 1000)
    private String description;
}

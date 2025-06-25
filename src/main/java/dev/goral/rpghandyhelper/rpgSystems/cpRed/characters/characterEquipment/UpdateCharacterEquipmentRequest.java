package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterEquipment;

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
public class UpdateCharacterEquipmentRequest {
    private Integer quantity;
    private CpRedCharacterItemStatus status;
    @Column(length = 1000)
    private String description;
}

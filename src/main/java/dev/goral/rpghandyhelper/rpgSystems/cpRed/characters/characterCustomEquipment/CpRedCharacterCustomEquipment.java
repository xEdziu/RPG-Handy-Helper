package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomEquipment;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterItem.CpRedCharacterItemStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customEquipments.CpRedCustomEquipments;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CpRedCharacterCustomEquipment {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterCustomEquipment_sequence",
            sequenceName = "cpRedCharacterCustomEquipment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCustomEquipment_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "customItem_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCustomEquipments customItem;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacters character;
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterItemStatus status;
    @Column(length = 1000)
    private String description;
}
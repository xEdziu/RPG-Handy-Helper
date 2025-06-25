package dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customWeapons;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsAvailability;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.skills.CpRedSkills;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.items.CpRedItemsQuality;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.weapons.CpRedWeaponsType;
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
public class CpRedCustomWeapons {
    @Id
    @SequenceGenerator(
            name="cpRedCustomWeapons_sequence",
            sequenceName = "cpRedCustomWeapons_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCustomWeapons_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(name = "game_id",
                referencedColumnName = "id",
                nullable = false)
    private Game gameId;
    @ManyToOne
    @JoinColumn(
            name="required_skill_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedSkills requiredSkillId;
    private String name;
    @Enumerated(EnumType.STRING)
    private CpRedWeaponsType type;//enum from weapons
    private Integer damage;
    private Integer magazineCapacity;
    private Short numberOfAttacks;
    private Short handType;
    private Boolean isHidden;
    @Enumerated(EnumType.STRING)
    private CpRedItemsQuality quality;
    private Integer price;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private Boolean isModifiable;
    private Short modSlots;
    @Column(length = 500)
    private String description;
}
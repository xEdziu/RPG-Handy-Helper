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
    private int damage = -1;
    private int magazineCapacity = -1;
    private short numberOfAttacks = -1;
    private short handType = -1;
    private boolean isHidden;
    @Enumerated(EnumType.STRING)
    private CpRedItemsQuality quality;
    private int price = -1;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private boolean isModifiable;
    @Column(length = 500)
    private String description;
}
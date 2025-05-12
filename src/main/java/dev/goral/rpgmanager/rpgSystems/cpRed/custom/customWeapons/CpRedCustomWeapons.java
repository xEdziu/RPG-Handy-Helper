package dev.goral.rpgmanager.rpgSystems.cpRed.custom.customWeapons;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.rpgSystems.cpRed.items.CpRedItemsAvailability;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.skills.CpRedSkills;
import dev.goral.rpgmanager.rpgSystems.cpRed.items.CpRedItemsQuality;
import dev.goral.rpgmanager.rpgSystems.cpRed.manual.weapons.CpRedWeaponsType;
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
    private int damage;
    private int magazineCapacity;
    private short numberOfAttacks;
    private short handType;
    private boolean isHidden;
    @Enumerated(EnumType.STRING)
    private CpRedItemsQuality quality;
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedItemsAvailability availability;
    private boolean isModifiable;
    private String description;
}
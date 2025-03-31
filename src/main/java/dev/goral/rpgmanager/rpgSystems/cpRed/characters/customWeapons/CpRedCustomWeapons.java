package dev.goral.rpgmanager.rpgSystems.cpRed.characters.customWeapons;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.item.CpRedItemAvailability;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.skills.CpRedSkills;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.weapons.CpRedWeaponsAmmunition;
import dev.goral.rpgmanager.rpgSystems.cpRed.characters.weapons.CpRedWeaponsType;
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
            name="cpRedCustomWeapon_sequence",
            sequenceName = "cpRedCustomWeapon_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCustomWeapon_sequence"
    )
    private long id;
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
    @Enumerated(EnumType.STRING)
    private CpRedWeaponsType type;//enum from weapons
    private int damage;
    private int magazineCapacity;
    @Enumerated(EnumType.STRING)
    private CpRedWeaponsAmmunition ammunition;
    private short la;
    private short hands;
    private boolean isHidden;
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedItemAvailability availability;
    private String description;
}
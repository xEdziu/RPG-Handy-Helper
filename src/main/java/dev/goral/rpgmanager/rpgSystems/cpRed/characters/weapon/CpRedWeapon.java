package dev.goral.rpgmanager.rpgSystems.cpRed.characters.weapon;

import dev.goral.rpgmanager.rpgSystems.cpRed.characters.skills.CpRedSkills;
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
public class CpRedWeapon {
    @Id
    @SequenceGenerator(
            name = "cpRedWeapon_sequence",
            sequenceName = "cpRedWeapon_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedWeapon_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name="skill_id",
            referencedColumnName = "id"
    )
    private CpRedSkills requireSkillId;
    @Enumerated(EnumType.STRING)
    private CpRedWeaponType type;
    private int damage; //quantity of d6
    private int magazineCapacity;
    @Enumerated(EnumType.STRING)
    private CpRedWeaponAmmunition ammunition;
    private short la;
    private short hands;
    private boolean isHidden;
    private int price;
    @Enumerated(EnumType.STRING)
    private CpRedWeaponAvailability availability;
    private String description;



}

package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCustomCriticalInjuries;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCriticalInjuries.CpRedCharacterCriticalInjuriesStatus;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.custom.customCriticalInjuries.CpRedCustomCriticalInjuries;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CpRedCharacterCustomCriticalInjuries {
    @Id
    @SequenceGenerator(name = "cpRedCharacterCustomCriticalInjuries_sequence",
            sequenceName = "cpRedCharacterCustomCriticalInjuries_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCustomCriticalInjuries_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "character_id",
            referencedColumnName = "id"
    )
    private CpRedCharacters character;
    @ManyToOne
    @JoinColumn(
            name = "custom_injuries_id",
            referencedColumnName = "id"
    )
    private CpRedCustomCriticalInjuries customInjuries;
    @Enumerated(EnumType.STRING)
    private CpRedCharacterCriticalInjuriesStatus status;
}
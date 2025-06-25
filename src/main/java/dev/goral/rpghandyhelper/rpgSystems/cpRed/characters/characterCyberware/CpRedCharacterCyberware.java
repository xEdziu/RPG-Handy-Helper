package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.characterCyberware;

import dev.goral.rpghandyhelper.rpgSystems.cpRed.characters.CpRedCharacters;
import dev.goral.rpghandyhelper.rpgSystems.cpRed.manual.cyberwares.CpRedCyberwares;
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
public class CpRedCharacterCyberware {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacterCyberware_sequence",
            sequenceName = "cpRedCharacterCyberware_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacterCyberware_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(
            name="cyberware_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCyberwares baseCyberware;
    @ManyToOne
    @JoinColumn(
            name="character_id",
            referencedColumnName = "id",
            nullable = false
    )
    private CpRedCharacters character;
    @Column(length = 1000)
    private String description;
}

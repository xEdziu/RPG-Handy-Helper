package dev.goral.rpgmanager.game;


import dev.goral.rpgmanager.user.User;
import dev.goral.rpgmanager.rpgSystems.RpgSystems;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Game {

    @Id
    @SequenceGenerator(
            name = "game_sequence",
            sequenceName = "game_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "game_sequence"
    )
    private Long id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User gameMaster;

    @ManyToOne
    @JoinColumn(
            name = "rpgSystem_id",
            referencedColumnName = "id",
            nullable = false
    )
    private RpgSystems rpgSystem;

    public Game(String name,
                String description,
                User gameMaster,
                RpgSystems rpgSystem) {
        this.name = name;
        this.description = description;
        this.gameMaster = gameMaster;
        this.rpgSystem = rpgSystem;
    }
}

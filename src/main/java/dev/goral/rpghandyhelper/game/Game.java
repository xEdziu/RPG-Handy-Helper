package dev.goral.rpghandyhelper.game;


import dev.goral.rpghandyhelper.user.User;
import dev.goral.rpghandyhelper.rpgSystems.RpgSystems;
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

    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = true
    )
    private User owner;

    @ManyToOne
    @JoinColumn(
            name = "rpgSystem_id",
            referencedColumnName = "id",
            nullable = false
    )
    private RpgSystems rpgSystem;

    @Enumerated(EnumType.STRING)
    private GameStatus status = GameStatus.ACTIVE;

    public Game(String name,
                String description,
                User owner,
                RpgSystems rpgSystem) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.rpgSystem = rpgSystem;
    }
}
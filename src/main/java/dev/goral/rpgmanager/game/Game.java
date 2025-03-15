package dev.goral.rpgmanager.game;


import dev.goral.rpgmanager.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter

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


    public Game() {
    }

    public Game(String name,
                String description,
                User gameMaster) {
        this.name = name;
        this.description = description;
        this.gameMaster = gameMaster;
    }

    public Game(Long id,
                String name,
                String description,
                User gameMaster) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gameMaster = gameMaster;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", gameMaster=" + gameMaster +
                '}';
    }
}

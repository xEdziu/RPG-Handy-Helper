package dev.goral.rpgmanager.game.gameUsers;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table
@Entity
@Getter
@Setter
public class GameUsers {
    @Id
    @SequenceGenerator(
            name = "game_users_sequence",
            sequenceName = "game_users_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "game_users_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id", nullable = false)
    private Game game;

    @Enumerated(EnumType.STRING)
    private GameUsersRole role;

    public GameUsers() {
    }

    public GameUsers(User user, Game game, GameUsersRole role) {
        this.user = user;
        this.game = game;
        this.role = role;
    }

    public GameUsers(Long id, User user, Game game, GameUsersRole role) {
        this.id = id;
        this.user = user;
        this.game = game;
        this.role = role;
    }

    @Override
    public String toString() {
        return "GameUsers{" +
                "id=" + id +
                ", user=" + user +
                ", game=" + game +
                ", role=" + role +
                '}';
    }
}

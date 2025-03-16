package dev.goral.rpgmanager.rpgSystems.cpRed.characters;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class CpRedCharacters {
    @Id
    @SequenceGenerator(
            name = "cpRedCharacters_sequence",
            sequenceName = "cpRedCharacters_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cpRedCharacters_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "game_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Game game;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = true
    )
    private User user;

    private String name;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private CpRedCharactersType type;

    private Integer expAll;
    private Integer expAvailable;
    private Integer casch;
    private String characterPhotoPath;

    public CpRedCharacters() {}

    public CpRedCharacters(Long id,
                           Game game,
                           User user,
                           String name,
                           String nickname,
                           CpRedCharactersType type,
                           Integer expAll,
                           Integer expAvailable,
                           Integer casch,
                           String characterPhotoPath) {
        this.id = id;
        this.game = game;
        this.user = user;
        this.name = name;
        this.nickname = nickname;
        this.type = type;
        this.expAll = expAll;
        this.expAvailable = expAvailable;
        this.casch = casch;
        this.characterPhotoPath = characterPhotoPath;
    }

    public CpRedCharacters(Long id,
                           Game game,
                           String name,
                           String nickname,
                           CpRedCharactersType type,
                           Integer expAll,
                           Integer expAvailable,
                           Integer casch,
                           String characterPhotoPath) {
        this.id = id;
        this.game = game;
        this.name = name;
        this.nickname = nickname;
        this.type = type;
        this.expAll = expAll;
        this.expAvailable = expAvailable;
        this.casch = casch;
        this.characterPhotoPath = characterPhotoPath;
    }

    @Override
    public String toString() {
        return "CpRedCharacters{" +
                "id=" + id +
                ", game=" + game +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", type=" + type +
                ", expAll=" + expAll +
                ", expAvailable=" + expAvailable +
                ", casch=" + casch +
                ", characterPhotoPath='" + characterPhotoPath + '\'' +
                '}';
    }
}

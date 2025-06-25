package dev.goral.rpghandyhelper.rpgSystems.cpRed.characters;

import dev.goral.rpghandyhelper.game.Game;
import dev.goral.rpghandyhelper.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    private Integer currentHp;
    private Integer maxHp;
    private Integer currentHumanity;
    private Integer maxHumanity;
    private Integer seriouslyWounded;
    private Integer survivability;
    private Integer expAll;
    private Integer expAvailable;
    private Integer cash;
    private String characterPhotoPath;
    private boolean alive = true;

    public CpRedCharacters(Long id,
                           Game game,
                           String name,
                           String nickname,
                           CpRedCharactersType type,
                           Integer expAll,
                           Integer expAvailable,
                           Integer cash,
                           String characterPhotoPath) {
        this.id = id;
        this.game = game;
        this.name = name;
        this.nickname = nickname;
        this.type = type;
        this.expAll = expAll;
        this.expAvailable = expAvailable;
        this.cash = cash;
        this.characterPhotoPath = characterPhotoPath;
    }
}

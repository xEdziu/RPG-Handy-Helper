package dev.goral.rpgmanager.notes;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "game_notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameNote {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "note_sequence")
    @SequenceGenerator(name = "note_sequence", sequenceName = "note_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }
}

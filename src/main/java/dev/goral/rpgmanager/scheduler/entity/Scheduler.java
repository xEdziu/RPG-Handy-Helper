package dev.goral.rpgmanager.scheduler.entity;

import dev.goral.rpgmanager.game.Game;
import dev.goral.rpgmanager.scheduler.enums.SchedulerStatus;
import dev.goral.rpgmanager.user.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "schedulers")
public class Scheduler {

    @Id
    @SequenceGenerator(
            name = "scheduler_sequence",
            sequenceName = "scheduler_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "scheduler_sequence"
    )
    private Long id;

    private String title;
    private LocalDateTime deadline;
    private Integer minimumSessionDurationMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @ToString.Exclude
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    @ToString.Exclude
    private User creator;

    @OneToMany(mappedBy = "scheduler", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<SchedulerDateRange> dateRanges = new ArrayList<>();

    @OneToMany(mappedBy = "scheduler", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<SchedulerParticipant> participants = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SchedulerStatus status = SchedulerStatus.AWAITING_AVAILABILITY;

    @Embedded
    private FinalDecision finalDecision;

    @Nullable
    private String googleCalendarLink;

    @Column(nullable = false)
    private boolean emailsSent = false;

}


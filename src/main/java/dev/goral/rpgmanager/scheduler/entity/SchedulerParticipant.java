package dev.goral.rpgmanager.scheduler.entity;

import dev.goral.rpgmanager.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scheduler_participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SchedulerParticipant {

    @Id
    @SequenceGenerator(
            name = "scheduler_participant_seq",
            sequenceName = "scheduler_participant_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "scheduler_participant_seq"
    )
    private Long id;

    private boolean notifiedByEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduler_id", nullable = false)
    @ToString.Exclude
    private Scheduler scheduler;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    @ToString.Exclude
    private User player;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<AvailabilitySlot> availabilitySlots = new ArrayList<>();
}

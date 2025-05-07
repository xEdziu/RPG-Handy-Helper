package dev.goral.rpgmanager.scheduler.entity;

import jakarta.persistence.*;
import lombok.*;
import dev.goral.rpgmanager.scheduler.enums.AvailabilityType;

import java.time.LocalDateTime;

@Entity
@Table(name = "availability_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AvailabilitySlot {

    @Id
    @SequenceGenerator(
            name = "availability_slot_seq",
            sequenceName = "availability_slot_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "availability_slot_seq"
    )
    private Long id;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private AvailabilityType availabilityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    @ToString.Exclude
    private SchedulerParticipant participant;
}

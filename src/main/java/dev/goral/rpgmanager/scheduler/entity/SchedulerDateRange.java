package dev.goral.rpgmanager.scheduler.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "scheduler_date_ranges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SchedulerDateRange {

    @Id
    @SequenceGenerator(
            name = "scheduler_date_range_seq",
            sequenceName = "scheduler_date_range_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "scheduler_date_range_seq"
    )
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalTime startTime; // np. 17:00
    private LocalTime endTime;   // np. 22:00

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduler_id", nullable = false)
    @ToString.Exclude
    private Scheduler scheduler;
}

package dev.goral.rpghandyhelper.scheduler.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduler_id", nullable = false)
    @ToString.Exclude
    private Scheduler scheduler;
}

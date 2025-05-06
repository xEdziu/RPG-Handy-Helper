package dev.goral.rpgmanager.scheduler.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeRange {
    private LocalTime startTime;
    private LocalTime endTime;
}

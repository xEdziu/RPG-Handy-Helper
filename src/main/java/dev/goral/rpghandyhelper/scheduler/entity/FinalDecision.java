package dev.goral.rpghandyhelper.scheduler.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FinalDecision {
    private LocalDateTime start;
    private LocalDateTime end;
}
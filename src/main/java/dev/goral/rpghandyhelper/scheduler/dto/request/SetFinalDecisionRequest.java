package dev.goral.rpghandyhelper.scheduler.dto.request;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Request object for setting the final decision in a scheduler.
 * Contains the scheduler ID and the start and end times of the final decision.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetFinalDecisionRequest {
    private Long schedulerId;
    private LocalDateTime start;
    private LocalDateTime end;
}

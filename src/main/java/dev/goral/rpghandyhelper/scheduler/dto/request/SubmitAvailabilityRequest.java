package dev.goral.rpghandyhelper.scheduler.dto.request;

import dev.goral.rpghandyhelper.scheduler.enums.AvailabilityType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Request object for submitting availability in a scheduler.
 * Contains the scheduler ID, player ID, and a list of availability slots.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAvailabilityRequest {
    private Long schedulerId;
    private Long playerId;
    private List<AvailabilitySlotDto> slots;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AvailabilitySlotDto {
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private AvailabilityType availabilityType;
    }
}

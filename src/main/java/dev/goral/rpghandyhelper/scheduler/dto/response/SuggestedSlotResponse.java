package dev.goral.rpghandyhelper.scheduler.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a response containing suggested time slots for scheduling.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuggestedSlotResponse {
    private List<TimeSlotDto> suggestedSlots;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSlotDto {
        private LocalDateTime start;
        private LocalDateTime end;
        private double weightOfTimeSlot;
    }
}


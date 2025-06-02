package dev.goral.rpghandyhelper.scheduler.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import dev.goral.rpghandyhelper.scheduler.enums.AvailabilityType;
import lombok.*;


/**
 * Response object for player availability in a scheduler.
 * Contains the player ID and a list of availability slots.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAvailabilityResponse {
    private Long playerId;
    private List<AvailabilitySlotDto> availability;

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

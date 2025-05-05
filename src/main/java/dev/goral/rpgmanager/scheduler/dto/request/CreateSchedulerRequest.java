package dev.goral.rpgmanager.scheduler.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Request object for creating a new scheduler.
 * Contains the title, deadline, minimum session duration, game ID, creator ID,
 * date ranges, and participant IDs.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSchedulerRequest {
    private String title;
    private LocalDateTime deadline;
    private Integer minimumSessionDurationMinutes;
    private Long gameId;
    private Long creatorId;
    private List<DateRangeDto> dateRanges;
    private List<Long> participantIds;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateRangeDto {
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalTime startTime;
        private LocalTime endTime;
    }
}

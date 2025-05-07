package dev.goral.rpgmanager.scheduler.dto.request;

import dev.goral.rpgmanager.scheduler.dto.common.TimeRangeDto;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Request object for creating a new scheduler.
 * <p>
 * This class contains the necessary fields to create a new scheduler, including the title, deadline,
 * minimum session duration, game ID, creator ID, date ranges, time ranges, and participant IDs.
 * </p>
 */
@Data
public class CreateSchedulerRequest {

    private String title;
    private LocalDateTime deadline;
    private Integer minimumSessionDurationMinutes;
    private Long gameId;
    private Long creatorId;
    private List<DateRangeDto> dateRanges;
    private List<TimeRangeDto> timeRanges;
    private List<Long> participantIds;

    @Data
    public static class DateRangeDto {
        private LocalDate startDate;
        private LocalDate endDate;
    }
}

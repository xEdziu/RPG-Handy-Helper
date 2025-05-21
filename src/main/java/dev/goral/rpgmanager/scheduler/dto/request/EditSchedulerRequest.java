package dev.goral.rpgmanager.scheduler.dto.request;

import dev.goral.rpgmanager.scheduler.dto.common.TimeRangeDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditSchedulerRequest {
    private Long schedulerId;
    private String title;
    private LocalDateTime deadline;
    private Integer minimumSessionDurationMinutes;
    private List<DateRangeDto> dateRanges;
    private List<TimeRangeDto> timeRanges;
    private List<Long> participantIds;

    @Data
    public static class DateRangeDto {
        private LocalDate startDate;
        private LocalDate endDate;
    }
}
package dev.goral.rpgmanager.scheduler.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditSchedulerRequest {
    private Long schedulerId;
    private String title;
    private LocalDateTime deadline;
    private Integer minimumSessionDurationMinutes;
    private List<CreateSchedulerRequest.DateRangeDto> dateRanges;
    private List<Long> participantIds;
}


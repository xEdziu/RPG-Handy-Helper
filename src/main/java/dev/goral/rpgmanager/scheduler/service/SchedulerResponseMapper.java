package dev.goral.rpgmanager.scheduler.service;

import dev.goral.rpgmanager.scheduler.dto.response.SchedulerResponse;
import dev.goral.rpgmanager.scheduler.entity.Scheduler;

public class SchedulerResponseMapper {

    public static SchedulerResponse mapToDto(Scheduler s) {
        return SchedulerResponse.builder()
                .id(s.getId())
                .title(s.getTitle())
                .deadline(s.getDeadline())
                .minimumSessionDurationMinutes(s.getMinimumSessionDurationMinutes())
                .gameId(s.getGame().getId())
                .creatorId(s.getCreator().getId())
                .googleCalendarLink(s.getGoogleCalendarLink())
                .dateRanges(
                        s.getDateRanges().stream()
                                .map(d -> new SchedulerResponse.DateRangeDto(
                                        d.getStartDate(), d.getEndDate(),
                                        d.getStartTime(), d.getEndTime()))
                                .toList()
                )
                .participants(
                        s.getParticipants().stream()
                                .map(p -> new SchedulerResponse.ParticipantDto(
                                        p.getId(),
                                        p.getPlayer().getId(),
                                        p.isNotifiedByEmail()
                                )).toList()
                )
                .status(s.getStatus())
                .missingAvailabilitiesCount(
                        (int) s.getParticipants().stream()
                                .filter(p -> p.getAvailabilitySlots() == null || p.getAvailabilitySlots().isEmpty())
                                .count()
                )

                .finalDecision(
                        s.getFinalDecision() == null ? null :
                                new SchedulerResponse.FinalDecisionDto(
                                        s.getFinalDecision().getStart(),
                                        s.getFinalDecision().getEnd()
                                )
                )
                .googleCalendarLink( s.getGoogleCalendarLink())
                .emailsSent(s.isEmailsSent())
                .build();
    }
}


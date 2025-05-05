package dev.goral.rpgmanager.scheduler.controller;

import dev.goral.rpgmanager.scheduler.dto.request.CreateSchedulerRequest;
import dev.goral.rpgmanager.scheduler.dto.request.SetFinalDecisionRequest;
import dev.goral.rpgmanager.scheduler.dto.request.SubmitAvailabilityRequest;
import dev.goral.rpgmanager.scheduler.dto.response.SchedulerResponse;
import dev.goral.rpgmanager.scheduler.service.SchedulerService;
import dev.goral.rpgmanager.security.CustomReturnables;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/authorized/schedulers")
@RequiredArgsConstructor
public class SchedulerController {

    private final SchedulerService schedulerService;

    @PostMapping("/create")
    public Map<String, Object> createScheduler(
            @RequestBody CreateSchedulerRequest request,
            @AuthenticationPrincipal Principal principal
    ) {
        SchedulerResponse scheduler = schedulerService.createScheduler(request, principal);
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Utworzono harmonogram.");
        response.put("scheduler", scheduler);
        return response;
    }

    @GetMapping("/forGame/{gameId}")
    public Map<String, Object> getSchedulersByGame(
            @PathVariable Long gameId,
            @AuthenticationPrincipal Principal principal
    ) {
        List<SchedulerResponse> schedulers = schedulerService.getSchedulersByGame(gameId, principal);
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano listę harmonogramów.");
        response.put("schedulers", schedulers);
        return response;
    }

    @GetMapping("/{schedulerId}")
    public Map<String, Object> getSchedulerById(
            @PathVariable Long schedulerId,
            @AuthenticationPrincipal Principal principal
    ) {
        SchedulerResponse scheduler = schedulerService.getSchedulerById(schedulerId, principal);
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano harmonogram.");
        response.put("scheduler", scheduler);
        return response;
    }

    @DeleteMapping("/delete/{schedulerId}")
    public Map<String, Object> deleteScheduler(
            @PathVariable Long schedulerId,
            @AuthenticationPrincipal Principal principal
    ) {
        schedulerService.deleteScheduler(schedulerId, principal);
        return CustomReturnables.getOkResponseMap("Scheduler został usunięty.");
    }

    @PutMapping("/setFinalDecision")
    public Map<String, Object> setFinalDecision(
            @RequestBody SetFinalDecisionRequest request,
            @AuthenticationPrincipal Principal principal
    ) {
        SchedulerResponse updated = schedulerService.setFinalDecision(request, principal);
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Ustawiono ostateczny termin.");
        response.put("scheduler", updated);
        return response;
    }

    @PostMapping("/submitAvailability")
    public Map<String, Object> submitAvailability(
            @RequestBody SubmitAvailabilityRequest request,
            @AuthenticationPrincipal Principal principal
    ) {
        schedulerService.submitAvailability(request, principal);
        return CustomReturnables.getOkResponseMap("Dostępność została zapisana.");
    }


}

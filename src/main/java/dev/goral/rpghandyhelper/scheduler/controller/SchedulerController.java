package dev.goral.rpghandyhelper.scheduler.controller;

import dev.goral.rpghandyhelper.scheduler.dto.request.CreateSchedulerRequest;
import dev.goral.rpghandyhelper.scheduler.dto.request.EditSchedulerRequest;
import dev.goral.rpghandyhelper.scheduler.dto.request.SetFinalDecisionRequest;
import dev.goral.rpghandyhelper.scheduler.dto.request.SubmitAvailabilityRequest;
import dev.goral.rpghandyhelper.scheduler.dto.response.PlayerAvailabilityResponse;
import dev.goral.rpghandyhelper.scheduler.dto.response.SchedulerResponse;
import dev.goral.rpghandyhelper.scheduler.dto.response.SuggestedSlotResponse;
import dev.goral.rpghandyhelper.scheduler.service.SchedulerService;
import dev.goral.rpghandyhelper.security.CustomReturnables;
import dev.goral.rpghandyhelper.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/authorized/schedulers")
@RequiredArgsConstructor
public class SchedulerController {

    private final SchedulerService schedulerService;

    /*
     =========================== SCHEDULER ===========================
     */


    @PostMapping("/create")
    public Map<String, Object> createScheduler(
            @RequestBody CreateSchedulerRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        SchedulerResponse scheduler = schedulerService.createScheduler(request, currentUser);
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Utworzono harmonogram.");
        response.put("scheduler", scheduler);
        return response;
    }

    @GetMapping("/forGame/{gameId}")
    public Map<String, Object> getSchedulersByGame(
            @PathVariable Long gameId,
            @AuthenticationPrincipal User currentUser
    ) {
        List<SchedulerResponse> schedulers = schedulerService.getSchedulersByGame(gameId, currentUser);
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano listę harmonogramów.");
        response.put("schedulers", schedulers);
        return response;
    }

    @GetMapping("/{schedulerId}")
    public Map<String, Object> getSchedulerById(
            @PathVariable Long schedulerId,
            @AuthenticationPrincipal User currentUser
    ) {
        SchedulerResponse scheduler = schedulerService.getSchedulerById(schedulerId, currentUser);
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano harmonogram.");
        response.put("scheduler", scheduler);
        return response;
    }

    @PutMapping("/edit")
    public Map<String, Object> editScheduler(
            @RequestBody EditSchedulerRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        SchedulerResponse scheduler = schedulerService.editScheduler(request, currentUser);
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Zaktualizowano scheduler.");
        response.put("scheduler", scheduler);
        return response;
    }


    @DeleteMapping("/delete/{schedulerId}")
    public Map<String, Object> deleteScheduler(
            @PathVariable Long schedulerId,
            @AuthenticationPrincipal User currentUser
    ) {
        schedulerService.deleteScheduler(schedulerId, currentUser);
        return CustomReturnables.getOkResponseMap("Scheduler został usunięty.");
    }

    @PutMapping("/setFinalDecision")
    public Map<String, Object> setFinalDecision(
            @RequestBody SetFinalDecisionRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        SchedulerResponse updated = schedulerService.setFinalDecision(request, currentUser);
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Ustawiono ostateczny termin.");
        response.put("scheduler", updated);
        return response;
    }

    @GetMapping("/suggestedSlots/{schedulerId}")
    public Map<String, Object> suggestTimeSlots(
            @PathVariable Long schedulerId,
            @AuthenticationPrincipal User currentUser
    ) {
        SuggestedSlotResponse responseData = schedulerService.suggestTimeSlots(schedulerId, currentUser);
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano proponowane przedziały.");
        response.put("suggestedSlots", responseData.getSuggestedSlots());
        return response;
    }

    @PostMapping("/sendFinalDecisionMails/{schedulerId}")
    public Map<String, Object> sendFinalDecisionMails(
            @PathVariable Long schedulerId,
            @AuthenticationPrincipal User currentUser
    ) {
        schedulerService.sendFinalDecisionMails(schedulerId, currentUser);
        return CustomReturnables.getOkResponseMap("Wysłano wiadomości e-mail z potwierdzeniem terminu.");
    }


    /*
     =========================== PLAYER AVAILABILITY ===========================
     */

    @PostMapping("/submitAvailability")
    public Map<String, Object> submitAvailability(
            @RequestBody SubmitAvailabilityRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        schedulerService.submitAvailability(request, currentUser);
        return CustomReturnables.getOkResponseMap("Dostępność została zapisana.");
    }

    @PutMapping("/editAvailability")
    public Map<String, Object> editAvailability(
            @RequestBody SubmitAvailabilityRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        schedulerService.editAvailability(request, currentUser);
        return CustomReturnables.getOkResponseMap("Dostępność została zaktualizowana.");
    }

    @GetMapping("/availability/{schedulerId}")
    public Map<String, Object> getPlayerAvailability(
            @PathVariable Long schedulerId,
            @AuthenticationPrincipal User currentUser
    ) {
        PlayerAvailabilityResponse responseData = schedulerService.getPlayerAvailability(schedulerId, currentUser);
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano dostępność gracza.");
        response.put("availability", responseData);
        return response;
    }

    @GetMapping("/availability/{schedulerId}/all")
    public Map<String, Object> getAllPlayerAvailability(
            @PathVariable Long schedulerId,
            @AuthenticationPrincipal User currentUser
    ) {
        List<PlayerAvailabilityResponse> responseData = schedulerService.getAllPlayerAvailability(schedulerId, currentUser);
        Map<String, Object> response = CustomReturnables.getOkResponseMap("Pobrano dostępność graczy.");
        response.put("availability", responseData);
        return response;
    }

}

package dev.goral.rpghandyhelper.mobile;

import dev.goral.rpghandyhelper.scheduler.service.SchedulerService;
import dev.goral.rpghandyhelper.user.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/mobile/v1/authorized")
public class MobileSchedulerController {

    private final MobileAuthTools mobileAuthTools;
    private final SchedulerService schedulerService;

    @GetMapping(path = "/schedulers/futureGames/{gameId}")
    public Map<String, Object> getFutureGamesForGame(@PathVariable String gameId) {
        User currentUser = mobileAuthTools.getUserFromJwt();
        return schedulerService.getFutureFinalDecisionsForGame(currentUser, Long.parseLong(gameId));
    }
}

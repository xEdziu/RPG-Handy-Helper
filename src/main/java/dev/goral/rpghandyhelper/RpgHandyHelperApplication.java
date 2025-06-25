package dev.goral.rpghandyhelper;

import dev.goral.rpghandyhelper.logs.LoggerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpgHandyHelperApplication {

    public static void main(String[] args) {
        // Initialize the logger service
        LoggerService.logInfo("Application is starting");
        SpringApplication.run(RpgHandyHelperApplication.class, args);
        // Log the application start
        LoggerService.logInfo("Application has started successfully");
        // Log the application stop
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LoggerService.logInfo("Application is stopping");
        }));
    }
}

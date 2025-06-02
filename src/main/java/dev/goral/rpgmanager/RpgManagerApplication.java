package dev.goral.rpgmanager;

import dev.goral.rpgmanager.logs.LoggerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpgManagerApplication {

    public static void main(String[] args) {
        // Initialize the logger service
        LoggerService.logInfo("Application has started");
        SpringApplication.run(RpgManagerApplication.class, args);
    }

}

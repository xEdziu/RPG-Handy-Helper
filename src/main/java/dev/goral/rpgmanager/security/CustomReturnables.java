package dev.goral.rpgmanager.security;

import org.springframework.http.HttpStatus;

import java.util.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class CustomReturnables {

    public static Map<String, Object> getOkResponseMap(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("error", HttpStatus.OK.value());
        response.put("timestamp", new Timestamp(new Date().getTime()));
        return response;
    }

    public static Map<String, Object> getErrorResponseMap(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("error", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", new Timestamp(new Date().getTime()));
        return response;
    }
}

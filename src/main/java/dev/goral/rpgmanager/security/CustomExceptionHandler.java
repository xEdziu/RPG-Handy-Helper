package dev.goral.rpgmanager.security;

import dev.goral.rpgmanager.security.exceptions.ForbiddenActionException;
import dev.goral.rpgmanager.security.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Timestamp(new Date().getTime()));
        response.put("message", ex.getMessage());
        response.put("description", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("error", HttpStatus.BAD_REQUEST.value());
        response.put("uri", request.getDescription(false).substring(4));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Timestamp(new Date().getTime()));
        response.put("message", ex.getMessage());
        response.put("description", HttpStatus.NOT_FOUND.getReasonPhrase());
        response.put("error", HttpStatus.NOT_FOUND.value());
        response.put("uri", request.getDescription(false).substring(4));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<Map<String, Object>> handleForbiddenAction(ForbiddenActionException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Timestamp(new Date().getTime()));
        response.put("message", ex.getMessage());
        response.put("description", HttpStatus.FORBIDDEN.getReasonPhrase());
        response.put("error", HttpStatus.FORBIDDEN.value());
        response.put("uri", request.getDescription(false).substring(4));
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<Map<String, Object>> handleInternalServerError(HttpServerErrorException.InternalServerError ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Timestamp(new Date().getTime()));
        response.put("message", ex.getMessage());
        response.put("description", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("uri", request.getDescription(false).substring(4));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalError.class)
    public ResponseEntity<Map<String, Object>> handleInternalError(InternalError ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Timestamp(new Date().getTime()));
        response.put("message", ex.getMessage());
        response.put("description", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("uri", request.getDescription(false).substring(4));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Timestamp(new Date().getTime()));
        response.put("message", ex.getMessage());
        response.put("description", HttpStatus.NOT_FOUND.getReasonPhrase());
        response.put("error", HttpStatus.NOT_FOUND.value());
        response.put("uri", request.getDescription(false).substring(4));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Add other exception handlers as needed
}

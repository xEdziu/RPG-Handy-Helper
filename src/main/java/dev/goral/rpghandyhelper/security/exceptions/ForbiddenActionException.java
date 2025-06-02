package dev.goral.rpghandyhelper.security.exceptions;

public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String message) {
        super(message);
    }
}

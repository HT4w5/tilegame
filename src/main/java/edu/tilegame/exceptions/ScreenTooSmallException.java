package edu.tilegame.exceptions;

public class ScreenTooSmallException extends RuntimeException {
    public ScreenTooSmallException() {
        super();
    }

    public ScreenTooSmallException(String message) {
        super(message);
    }

    public ScreenTooSmallException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScreenTooSmallException(Throwable cause) {
        super(cause);
    }
}

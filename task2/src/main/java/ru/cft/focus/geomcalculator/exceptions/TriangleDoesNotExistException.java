package ru.cft.focus.geomcalculator.exceptions;

public class TriangleDoesNotExistException extends Exception {
    public TriangleDoesNotExistException() {
        super();
    }

    public TriangleDoesNotExistException(String message) {
        super(message);
    }

    public TriangleDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public TriangleDoesNotExistException(Throwable cause) {
        super(cause);
    }
}

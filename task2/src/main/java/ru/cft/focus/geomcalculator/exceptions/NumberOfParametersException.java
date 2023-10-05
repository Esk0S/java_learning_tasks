package ru.cft.focus.geomcalculator.exceptions;

public class NumberOfParametersException extends Exception {
    public NumberOfParametersException() {
        super();
    }

    public NumberOfParametersException(String message) {
        super(message);
    }

    public NumberOfParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public NumberOfParametersException(Throwable cause) {
        super(cause);
    }
}

package ru.cft.focus.client.exception;

public class ClientNotConnectedException extends Exception {
    public ClientNotConnectedException() {
        super();
    }

    public ClientNotConnectedException(String message) {
        super(message);
    }

    public ClientNotConnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientNotConnectedException(Throwable cause) {
        super(cause);
    }
}

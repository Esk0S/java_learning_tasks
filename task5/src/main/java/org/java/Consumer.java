package org.java;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Consumer extends Thread {
    private final Storage storage;
    private final int timeout;
    private final int id;
    private static final int SECOND_IN_MS = 1000;

    public Consumer(Storage storage, int timeout, int id) {
        this.storage = storage;
        this.timeout = timeout * SECOND_IN_MS;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            storage.get(id);
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                log.error(() -> "Consumer " + id + ": ", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}

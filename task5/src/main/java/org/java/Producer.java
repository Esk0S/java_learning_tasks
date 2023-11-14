package org.java;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Producer extends Thread {
    private final Storage storage;
    private final int timeout;
    private final int id;
    private static int productNumber = 0;
    private static final Object o = new Object();

    public Producer(Storage storage, int timeout, int id) {
        this.storage = storage;
        this.timeout = timeout * 1000;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (o) {
                storage.put(id, ++productNumber);
            }
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                log.error(() -> "Producer " + id + ": ", e);
                Thread.currentThread().interrupt();
            }
        }
    }

}

package org.java;

import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;

@Log4j2
public class Storage {
    private final int size;
    private final LinkedList<Integer> products;

    public Storage(int size) {
        this.size = size;
        this.products = new LinkedList<>();
    }

    public synchronized void get(int consumerId) {
        log.info(() -> "Consumer " + consumerId + " is waiting");
        while (products.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                log.error(() -> "Consumer " + consumerId + ": ", e);
                Thread.currentThread().interrupt();
            }
        }
        log.info(() -> "Consumer " + consumerId + " started buying");

        int product = products.pop();
        log.info(() -> "Consumer " + consumerId + " bought item number " + product + "; Items on storage: " + products.size());
        notifyAll();
    }

    public synchronized void put(int producerId, int productId) {
        log.info(() -> "Producer " + producerId + ", product id: " + productId + ", is waiting");
        while (products.size() >= size) {
            try {
                wait();
            } catch (InterruptedException e) {
                log.error(() -> "Producer " + producerId + ": ", e);
                Thread.currentThread().interrupt();
            }
        }
        log.info(() -> "Producer " + producerId + " started production");

        products.add(productId);
        log.info(() -> "Producer " + producerId + " added item number " + productId + "; Items on storage: " + products.size());
        notifyAll();

    }
}

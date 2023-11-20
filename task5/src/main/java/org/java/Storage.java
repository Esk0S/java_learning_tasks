package org.java;

import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;

@Log4j2
public class Storage {
    private final int size;
    private final LinkedList<Integer> products;
    private static final String PRODUCER_STRING = "Producer ";
    private static final String CONSUMER_STRING = "Consumer ";

    public Storage(int size) {
        this.size = size;
        this.products = new LinkedList<>();
    }

    public synchronized void get(int consumerId) {
        log.info(() -> CONSUMER_STRING + consumerId + " is waiting");
        while (products.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                log.error(() -> CONSUMER_STRING + consumerId + ": ", e);
                Thread.currentThread().interrupt();
            }
        }
        log.info(() -> CONSUMER_STRING + consumerId + " started buying");

        int product = products.pop();
        log.info(() -> CONSUMER_STRING + consumerId + " bought item number " + product);
        log.info(() -> "Items on storage: " + products.size());
        notifyAll();
    }

    public synchronized void put(int producerId, int productId) {
        log.info(() -> PRODUCER_STRING + producerId + ", product id: " + productId + ", is waiting");
        while (products.size() >= size) {
            try {
                wait();
            } catch (InterruptedException e) {
                log.error(() -> PRODUCER_STRING + producerId + ": ", e);
                Thread.currentThread().interrupt();
            }
        }
        log.info(() -> PRODUCER_STRING + producerId + " started production");

        products.add(productId);
        log.info(() -> PRODUCER_STRING + producerId + " added item number " + productId);
        log.info(() -> "Items on storage: " + products.size());
        notifyAll();

    }
}

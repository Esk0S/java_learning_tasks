package org.java;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class Main {
    private static final int NUM_THREADS = 8;
    private static final int NUM_OF_ELEMENTS = 10_000_000;
    private static final int ITEMS_PER_THREAD = NUM_OF_ELEMENTS / NUM_THREADS;

    public static void main(String[] args) {
        AtomicReference<Double> counter = new AtomicReference<>(0.0);

        Task[] task = new Task[NUM_THREADS];
        log.info(() -> "Num of threads: " + NUM_THREADS + ", items per thread: " + ITEMS_PER_THREAD);
        for (int threadID = 0; threadID < NUM_THREADS; threadID++) {

            int lowerBound = threadID * ITEMS_PER_THREAD + 1;
            int upperBound = (threadID == NUM_THREADS - 1) ? (NUM_OF_ELEMENTS) : (lowerBound + ITEMS_PER_THREAD - 1);

            task[threadID] = new Task(lowerBound, upperBound);
            task[threadID].start();
        }

        for (int threadID = 0; threadID < NUM_THREADS; threadID++) {
            try {
                task[threadID].join();
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
            int i = threadID;
            counter.updateAndGet(result -> result + task[i].getResult());
        }

        log.info(() -> "result: " + counter.get());
    }
}
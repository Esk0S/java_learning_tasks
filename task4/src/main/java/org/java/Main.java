package org.java;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class Main {
    public static void main(String[] args) {
        CliHandler cliHandler = new CliHandler(args);
        int numElements = cliHandler.getNumElements();
        int numThreads = Runtime.getRuntime().availableProcessors() / 2;
        int itemsPerThread = numElements / numThreads;

        Task[] task = new Task[numThreads];
        log.info(() -> "Num of elements: " + numElements + ", Num of threads: " + numThreads + ", items per thread: " + itemsPerThread);
        for (int threadID = 0; threadID < numThreads; threadID++) {

            int lowerBound = threadID * itemsPerThread + 1;
            int upperBound = (threadID == numThreads - 1) ? (numElements) : (lowerBound + itemsPerThread - 1);

            task[threadID] = new Task(lowerBound, upperBound);
            task[threadID].start();
        }

        AtomicReference<Double> counter = new AtomicReference<>(0.0);
        for (var thread : task) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }

            counter.updateAndGet(result -> result + thread.getResult());
        }

        log.info(() -> "result: " + counter.get());
    }
}
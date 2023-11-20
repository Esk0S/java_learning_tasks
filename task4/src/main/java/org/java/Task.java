package org.java;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Task extends Thread {
    private final int lowerBound;
    private final int upperBound;
    private double funcSum = 0;

    public Task(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    private double function(int n) {
        return 1.0 / n - 1.0 / (n + 1);
    }

    @Override
    public void run() {
        log.info(() -> "start thread: " + "lowerBound: " + lowerBound + ", upperBound: " + upperBound);
        for (int i = lowerBound; i <= upperBound; i++) {
            funcSum += function(i);
        }

        log.info(() -> "end thread: funcSum: " + funcSum);
    }

    public double getResult() {
        return funcSum;
    }
}

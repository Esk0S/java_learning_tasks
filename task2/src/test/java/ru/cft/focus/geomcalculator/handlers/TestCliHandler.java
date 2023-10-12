package ru.cft.focus.geomcalculator.handlers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestCliHandler {
    @Test
    void getInputPath() {
        String[] args = new String[]{"-c", "-i", "task2data/circle.txt"};
        CliHandler cliHandler = new CliHandler(args);
        String expected = "task2data/circle.txt";
        String actual = cliHandler.getInputPath();
        assertEquals(expected, actual);
    }

    @Test
    void isFileOutput() {
        String[] args = new String[]{"-f", "-i", "task2data/circle.txt"};
        CliHandler cliHandler = new CliHandler(args);
        boolean actual = cliHandler.isFileOutput();
        assertTrue(actual);
    }
}

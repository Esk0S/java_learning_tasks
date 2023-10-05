package ru.cft.focus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.cft.focus.geomcalculator.handlers.CliHandler;

class TestCliHandler {
    @Test
    void getInputPath() {
        String[] args = new String[]{"-c", "-i", "task2data/circle.txt"};
        CliHandler cliHandler = new CliHandler(args);
        String expected = "task2data/circle.txt";
        String actual = cliHandler.getInputPath();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void isFileOutput() {
        String[] args = new String[]{"-f", "-i", "task2data/circle.txt"};
        CliHandler cliHandler = new CliHandler(args);
        boolean actual = cliHandler.isFileOutput();
        Assertions.assertTrue(actual);
    }
}

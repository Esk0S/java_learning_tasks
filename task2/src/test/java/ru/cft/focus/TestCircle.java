package ru.cft.focus;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;
import ru.cft.focus.geomcalculator.shapes.Circle;

class TestCircle {
    @Test
    void getRadius() throws NumberOfParametersException {
        Circle circle = new Circle(new double[]{3});
        double expected = 3;
        double actual = circle.getRadius();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getDiameter() throws NumberOfParametersException {
        Circle circle = new Circle(new double[]{3});
        double expected = 6;
        double actual = circle.gerDiameter();
        Assertions.assertEquals(expected, actual);
    }
}

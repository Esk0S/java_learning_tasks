package ru.cft.focus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;
import ru.cft.focus.geomcalculator.shapes.Rectangle;

class TestRectangle {
    @Test
    void getDiagonal() throws NumberOfParametersException {
        double sideA = 3;
        double sideB = 5;
        Rectangle rectangle = new Rectangle(new double[]{sideA, sideB});
        double expected = Math.sqrt(sideA * sideA + sideB * sideB);
        double actual = rectangle.getDiagonal();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getLength() throws NumberOfParametersException {
        Rectangle rectangle = new Rectangle(new double[]{3, 5});
        double expected = 5;
        double actual = rectangle.getLength();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getWidth() throws NumberOfParametersException {
        Rectangle rectangle = new Rectangle(new double[]{3, 5});
        double expected = 3;
        double actual = rectangle.getWidth();
        Assertions.assertEquals(expected, actual);
    }
}

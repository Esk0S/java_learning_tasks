package ru.cft.focus.geomcalculator.shapes;

import org.junit.jupiter.api.Test;
import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestRectangle {
    @Test
    void getDiagonal() throws NumberOfParametersException {
        double sideA = 3;
        double sideB = 5;
        Rectangle rectangle = new Rectangle(new double[]{sideA, sideB});

        double expected = Math.sqrt(sideA * sideA + sideB * sideB);
        double actual = rectangle.getDiagonal();
        assertEquals(expected, actual);
    }

    @Test
    void getLength() throws NumberOfParametersException {
        Rectangle rectangle = new Rectangle(new double[]{3, 5});

        double expected = 5;
        double actual = rectangle.getLength();
        assertEquals(expected, actual);
    }

    @Test
    void getWidth() throws NumberOfParametersException {
        Rectangle rectangle = new Rectangle(new double[]{3, 5});

        double expected = 3;
        double actual = rectangle.getWidth();
        assertEquals(expected, actual);
    }

    @Test
    void getPerimeter() throws NumberOfParametersException {
        Rectangle rectangle = new Rectangle(new double[]{2, 5});

        double expected = 14;
        double actual = rectangle.getPerimeter();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    void getArea() throws NumberOfParametersException {
        Rectangle rectangle = new Rectangle(new double[]{2, 5});

        double expected = 10;
        double actual = rectangle.getArea();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    void getName() throws NumberOfParametersException {
        Rectangle rectangle = new Rectangle(new double[]{2, 5});

        String expected = "Rectangle";
        String actual = rectangle.getName();
        assertEquals(expected, actual);
    }

    @Test
    void testInvalidNumberOfParameters() {
        double[] params = {5.0};
        assertThrows(NumberOfParametersException.class, () -> new Rectangle(params));
    }
}

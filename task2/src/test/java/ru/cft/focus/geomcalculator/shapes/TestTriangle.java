package ru.cft.focus.geomcalculator.shapes;

import org.junit.jupiter.api.Test;
import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;
import ru.cft.focus.geomcalculator.exceptions.TriangleDoesNotExistException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestTriangle {
    @Test
    void getAlphaAngle() throws NumberOfParametersException, TriangleDoesNotExistException {
        double sideA = 2;
        double sideB = 3;
        double sideC = 4;
        Triangle triangle = new Triangle(new double[]{sideA, sideB, sideC});
        double expected = 0.505;
        double actual = triangle.getAlphaAngle();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    void getBetaAngle() throws NumberOfParametersException, TriangleDoesNotExistException {
        double sideA = 2;
        double sideB = 3;
        double sideC = 4;
        Triangle triangle = new Triangle(new double[]{sideA, sideB, sideC});
        double expected = 0.812;
        double actual = triangle.getBetaAngle();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    void getGammaAngle() throws NumberOfParametersException, TriangleDoesNotExistException {
        double sideA = 2;
        double sideB = 3;
        double sideC = 4;
        Triangle triangle = new Triangle(new double[]{sideA, sideB, sideC});
        double expected = 1.823;
        double actual = triangle.getGammaAngle();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    void getPerimeter() throws NumberOfParametersException, TriangleDoesNotExistException {
        Triangle triangle = new Triangle(new double[]{2, 7, 8});

        double expected = 17;
        double actual = triangle.getPerimeter();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    void getArea() throws NumberOfParametersException, TriangleDoesNotExistException {
        Triangle triangle = new Triangle(new double[]{2, 7, 8});

        double expected = 6.437;
        double actual = triangle.getArea();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    void getName() throws NumberOfParametersException, TriangleDoesNotExistException {
        Triangle triangle = new Triangle(new double[]{2, 7, 8});

        String expected = "Triangle";
        String actual = triangle.getName();
        assertEquals(expected, actual);
    }

    @Test
    void testInvalidNumberOfParameters() {
        double[] params = {5.0, 3.0, 5.0, 9.0};
        assertThrows(NumberOfParametersException.class, () -> new Triangle(params));
    }

    @Test
    void testTriangleDoesNotExistException() {
        double[] params = {1.0, 2.0, 3.0};
        assertThrows(TriangleDoesNotExistException.class, () -> new Triangle(params));
    }
}

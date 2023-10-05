package ru.cft.focus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;
import ru.cft.focus.geomcalculator.exceptions.TriangleDoesNotExistException;
import ru.cft.focus.geomcalculator.shapes.Triangle;

class TestTriangle {
    @Test
    void getAlphaAngle() throws NumberOfParametersException, TriangleDoesNotExistException {
        double sideA = 2;
        double sideB = 3;
        double sideC = 4;
        Triangle triangle = new Triangle(new double[]{sideA, sideB, sideC});
        double expected = Math.acos(((sideB * sideB) + (sideC * sideC) - (sideA * sideA))
                / (2 * sideB * sideC));
        ;
        double actual = triangle.getAlphaAngle();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getBetaAngle() throws NumberOfParametersException, TriangleDoesNotExistException {
        double sideA = 2;
        double sideB = 3;
        double sideC = 4;
        Triangle triangle = new Triangle(new double[]{sideA, sideB, sideC});
        double expected = Math.acos(((sideA * sideA) + (sideC * sideC) - (sideB * sideB))
                / (2 * sideA * sideC));
        double actual = triangle.getBetaAngle();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getGammaAngle() throws NumberOfParametersException, TriangleDoesNotExistException {
        double sideA = 2;
        double sideB = 3;
        double sideC = 4;
        Triangle triangle = new Triangle(new double[]{sideA, sideB, sideC});
        double expected = Math.acos(((sideA * sideA) + (sideB * sideB) - (sideC * sideC))
                / (2 * sideA * sideB));
        double actual = triangle.getGammaAngle();
        Assertions.assertEquals(expected, actual);
    }
}

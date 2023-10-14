package ru.cft.focus.geomcalculator.shapes;

import org.junit.jupiter.api.Test;
import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestCircle {
    @Test
    void getRadius() throws NumberOfParametersException {
        Circle circle = new Circle(new double[]{3});

        double expected = 3;
        double actual = circle.getRadius();
        assertEquals(expected, actual);
    }

    @Test
    void getDiameter() throws NumberOfParametersException {
        Circle circle = new Circle(new double[]{3});
        double expected = 6;
        double actual = circle.gerDiameter();
        assertEquals(expected, actual);
    }

    @Test
    void getName() throws NumberOfParametersException {
        Circle circle = new Circle(new double[]{3});

        String expected = "Circle";
        String actual = circle.getName();
        assertEquals(expected, actual);
    }

    @Test
    void getPerimeter() throws NumberOfParametersException {
        Circle circle = new Circle(new double[]{3});

        double expected = 18.849;
        double actual = circle.getPerimeter();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    void getArea() throws NumberOfParametersException {
        Circle circle = new Circle(new double[]{3});

        double expected = 28.274;
        double actual = circle.getArea();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    void printShape() throws NumberOfParametersException {
        Circle circle = new Circle(new double[]{3});

        String expected = """
                Figure type: Circle
                Area: 28.27 sqr mm
                Perimeter: 18.85 mm
                Radius: 3.00 mm
                Diameter: 6.00 mm
                """;
        String actual = circle.printShape();
        assertEquals(expected, actual);
    }

    @Test
    void testInvalidNumberOfParameters() {
        double[] params = {5.0, 3.0};
        assertThrows(NumberOfParametersException.class, () -> new Circle(params));
    }
}

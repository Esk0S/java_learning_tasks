package ru.cft.focus.geomcalculator.shapes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import ru.cft.focus.geomcalculator.exceptions.TriangleDoesNotExistException;

class TestShapeFactory {
    @Test
    void createCircle() {
        ShapeFactory factory = new ShapeFactory();
        double[] params = {5.0};
        try {
            Shape shape = factory.createShape("CIRCLE", params);
            assertTrue(shape instanceof Circle);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void createRectangle() {
        ShapeFactory factory = new ShapeFactory();
        double[] params = {3.0, 4.0};
        try {
            Shape shape = factory.createShape("RECTANGLE", params);
            assertTrue(shape instanceof Rectangle);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void createTriangle() {
        ShapeFactory factory = new ShapeFactory();
        double[] params = {3.0, 4.0, 5.0};
        try {
            Shape shape = factory.createShape("TRIANGLE", params);
            assertTrue(shape instanceof Triangle);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void createInvalidShape() {
        ShapeFactory factory = new ShapeFactory();
        double[] params = {1.0, 2.0, 3.0}; // некорректные параметры
        assertThrows(IllegalArgumentException.class, () -> factory.createShape("INVALID_SHAPE", params));
    }

    @Test
    void createInvalidTriangle() {
        ShapeFactory factory = new ShapeFactory();
        double[] params = {1.0, 2.0, 10.0}; // некорректные стороны для треугольника
        assertThrows(TriangleDoesNotExistException.class, () -> factory.createShape("TRIANGLE", params));
    }
}


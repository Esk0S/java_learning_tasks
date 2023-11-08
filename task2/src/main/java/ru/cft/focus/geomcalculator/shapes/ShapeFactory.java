package ru.cft.focus.geomcalculator.shapes;

import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;
import ru.cft.focus.geomcalculator.exceptions.TriangleDoesNotExistException;

public class ShapeFactory {
    public Shape createShape(String figureType, double[] params) throws
            NumberOfParametersException, TriangleDoesNotExistException {

        ShapeNames shapeName = ShapeNames.valueOf(figureType);
        return switch (shapeName) {
            case CIRCLE -> new Circle(params);
            case RECTANGLE -> new Rectangle(params);
            case TRIANGLE -> new Triangle(params);
        };
    }

}

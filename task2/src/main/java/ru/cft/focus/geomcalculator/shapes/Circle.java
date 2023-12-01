package ru.cft.focus.geomcalculator.shapes;

import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;

public class Circle extends Shape {
    private double diameter;
    private final double radius;

    private static final String NAME_OF_SHAPE = "Circle";
    public static final String NUM_OF_PARAMS_EXCEPTION = "The number of arguments must be one";

    public Circle(final double[] params) throws NumberOfParametersException {
        if (params.length != 1) {
            throw new NumberOfParametersException(NUM_OF_PARAMS_EXCEPTION);
        } else {
            int radiusParamNumber = 0;
            radius = params[radiusParamNumber];
            name = NAME_OF_SHAPE;

            calculatePerimeter();
            calculateArea();
            calculateDiameter();
        }
    }

    private void calculatePerimeter() {
        perimeter = 2 * Math.PI * radius;
    }

    private void calculateArea() {
        area = Math.PI * radius * radius;
    }

    private void calculateDiameter() {
        diameter = 2 * radius;
    }

    public double getRadius() {
        return radius;
    }

    public double getDiameter() {
        return diameter;
    }

    @Override
    public String printShape() {
        return printCommon() +
                String.format("""
                                Radius: %.2f mm
                                Diameter: %.2f mm
                                """,
                        getRadius(), getDiameter());
    }
}

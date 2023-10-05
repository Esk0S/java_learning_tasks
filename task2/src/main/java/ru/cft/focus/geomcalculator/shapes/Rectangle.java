package ru.cft.focus.geomcalculator.shapes;

import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;

public class Rectangle extends Shape {
    private double diagonal;
    private double length;
    private double width;
    private final double sideA;
    private final double sideB;
    private static final String NAME_OF_SHAPE = "Rectangle";
    private static final int SIDE_A_PARAM_NAME = 0;
    private static final int SIDE_B_PARAM_NAME = 1;
    public static final String NUMS_OF_PARAMS_EXCEPTION = "The number of arguments must be two";

    public Rectangle(double[] params) throws NumberOfParametersException {
        if (params.length != 2) {
            throw new NumberOfParametersException(NUMS_OF_PARAMS_EXCEPTION);
        } else {
            sideA = params[SIDE_A_PARAM_NAME];
            sideB = params[SIDE_B_PARAM_NAME];
            name = NAME_OF_SHAPE;

            calculatePerimeter();
            calculatorArea();
            calculateDiagonal();
            calculateLength();
            calculateWidth();
        }
    }

    private void calculatePerimeter() {
        perimeter = 2 * (sideA + sideB);
    }

    private void calculatorArea() {
        area = sideA * sideB;
    }

    private void calculateDiagonal() {
        diagonal = Math.sqrt(sideA * sideA + sideB * sideB);
    }

    private void calculateLength() {
        length = Math.max(sideA, sideB);
    }

    private void calculateWidth() {
        width = Math.min(sideA, sideB);
    }

    public double getDiagonal() {
        return diagonal;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }
}

package ru.cft.focus.geomcalculator.shapes;

import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;

public class Rectangle extends Shape {
    private double diagonal;
    private double length;
    private double width;
    private final double sideA;
    private final double sideB;
    private static final String NAME_OF_SHAPE = "Rectangle";

    public static final String NUM_OF_PARAMS_EXCEPTION = "The number of arguments must be two";

    public Rectangle(double[] params) throws NumberOfParametersException {
        if (params.length != 2) {
            throw new NumberOfParametersException(NUM_OF_PARAMS_EXCEPTION);
        } else {
            int sideAParamNumber = 0;
            int sideBParamNumber = 1;
            sideA = params[sideAParamNumber];
            sideB = params[sideBParamNumber];
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

    @Override
    public String printShape() {
        return printCommon() +
                String.format("""
                                Diagonal length: %.2f mm
                                Length: %.2f mm
                                Width: %.2f mm
                                """,
                        getDiagonal(), getLength(), getWidth());
    }
}

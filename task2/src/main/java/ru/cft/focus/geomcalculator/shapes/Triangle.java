package ru.cft.focus.geomcalculator.shapes;

import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;
import ru.cft.focus.geomcalculator.exceptions.TriangleDoesNotExistException;

public class Triangle extends Shape {
    private double alphaAngle;
    private double betaAngle;
    private double gammaAngle;
    private final double sideLengthA;
    private final double sideLengthB;
    private final double sideLengthC;
    private static final int SIDE_LENGTH_A = 0;
    private static final int SIDE_LENGTH_B = 1;
    private static final int SIDE_LENGTH_C = 2;
    private static final String NAME_OF_SHAPE = "Triangle";
    private static final String TRIANGLE_DOES_NOT_EXIST_EXCEPTION = "Triangle with these sides does not exist";
    public static final String NUMS_OF_PARAMS_EXCEPTION = "The number of arguments must be three";

    public Triangle(double[] params) throws NumberOfParametersException, TriangleDoesNotExistException {
        if (params.length != 3) {
            throw new NumberOfParametersException(NUMS_OF_PARAMS_EXCEPTION);
        } else if (params[SIDE_LENGTH_A] + params[SIDE_LENGTH_B] <= params[SIDE_LENGTH_C]) {
            throw new TriangleDoesNotExistException(TRIANGLE_DOES_NOT_EXIST_EXCEPTION);
        } else {
            sideLengthA = params[SIDE_LENGTH_A];
            sideLengthB = params[SIDE_LENGTH_B];
            sideLengthC = params[SIDE_LENGTH_C];
            name = NAME_OF_SHAPE;

            calculatePerimeter();
            calculateArea();
            calculateAngles();
        }
    }

    private void calculateAngles() {
        alphaAngle = Math.acos(((sideLengthB * sideLengthB) + (sideLengthC * sideLengthC) - (sideLengthA * sideLengthA))
                / (2 * sideLengthB * sideLengthC));
        betaAngle = Math.acos(((sideLengthA * sideLengthA) + (sideLengthC * sideLengthC) - (sideLengthB * sideLengthB))
                / (2 * sideLengthA * sideLengthC));
        gammaAngle = Math.acos(((sideLengthA * sideLengthA) + (sideLengthB * sideLengthB) - (sideLengthC * sideLengthC))
                / (2 * sideLengthA * sideLengthB));
    }

    private void calculatePerimeter() {
        perimeter = sideLengthA + sideLengthB + sideLengthC;
    }

    private void calculateArea() {
        double p = perimeter / 2;
        area = Math.sqrt(p * (p - sideLengthA) * (p - sideLengthB) * (p - sideLengthC));
    }

    public double getAlphaAngle() {
        return alphaAngle;
    }

    public double getBetaAngle() {
        return betaAngle;
    }

    public double getGammaAngle() {
        return gammaAngle;
    }
}

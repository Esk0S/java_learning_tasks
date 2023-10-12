package ru.cft.focus.geomcalculator.shapes;

import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;
import ru.cft.focus.geomcalculator.exceptions.TriangleDoesNotExistException;

public class Triangle extends Shape {
    private double alphaAngle;
    private double betaAngle;
    private double gammaAngle;
    private final double sideA;
    private final double sideB;
    private final double sideC;

    private static final String NAME_OF_SHAPE = "Triangle";
    public static final String NUM_OF_PARAMS_EXCEPTION = "The number of arguments must be three";

    public Triangle(double[] params) throws NumberOfParametersException, TriangleDoesNotExistException {
        int sideAParamNumber = 0;
        int sideBParamNumber = 1;
        int sideCParamNumber = 2;
        if (params.length != 3) {
            throw new NumberOfParametersException(NUM_OF_PARAMS_EXCEPTION);
        } else if (params[sideAParamNumber] + params[sideBParamNumber] <= params[sideCParamNumber]) {
            String triangleDoesNotExistException =
                    "Triangle with sides " + params[sideAParamNumber] + ", " + params[sideBParamNumber] +
                            ", " + params[sideCParamNumber] + " does not exist";
            throw new TriangleDoesNotExistException(triangleDoesNotExistException);
        } else {
            this.sideA = params[sideAParamNumber];
            this.sideB = params[sideBParamNumber];
            this.sideC = params[sideCParamNumber];
            name = NAME_OF_SHAPE;

            calculatePerimeter();
            calculateArea();
            calculateAngles();
        }
    }

    private void calculateAngles() {
        alphaAngle = Math.acos(((sideB * sideB) + (sideC * sideC) - (sideA * sideA))
                / (2 * sideB * sideC));
        betaAngle = Math.acos(((sideA * sideA) + (sideC * sideC) - (sideB * sideB))
                / (2 * sideA * sideC));
        gammaAngle = Math.acos(((sideA * sideA) + (sideB * sideB) - (sideC * sideC))
                / (2 * sideA * sideB));
    }

    private void calculatePerimeter() {
        perimeter = sideA + sideB + sideC;
    }

    private void calculateArea() {
        double p = perimeter / 2;
        area = Math.sqrt(p * (p - sideA) * (p - sideB) * (p - sideC));
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

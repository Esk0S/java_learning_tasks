package ru.cft.focus.geomcalculator.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;
import ru.cft.focus.geomcalculator.exceptions.TriangleDoesNotExistException;
import ru.cft.focus.geomcalculator.shapes.Circle;
import ru.cft.focus.geomcalculator.shapes.Rectangle;
import ru.cft.focus.geomcalculator.shapes.Shape;
import ru.cft.focus.geomcalculator.shapes.Triangle;

public class OutputHandler {
    private static final String SHAPE_SWITCH_DEFAULT = "Unknown shape type: ";
    private static final String CIRCLE = "CIRCLE";
    private static final String RECTANGLE = "RECTANGLE";
    private static final String TRIANGLE = "TRIANGLE";
    private static final String NUM_OF_PARAMS_EXCEPTION = "Invalid number of parameters: ";
    private static final String TRIANGLE_DOES_NOT_EXIST_EXCEPTION = "";
    private static final Logger logger = LogManager.getLogger(OutputHandler.class);
    private final String figureType;
    private final double[] shapeParams;

    public OutputHandler(String figureType, double[] shapeParams) {
        this.figureType = figureType;
        this.shapeParams = shapeParams;
    }

    public void outputShape() {
        switch (figureType) {
            case CIRCLE -> {
                try {
                    String info = outputCircle(new Circle(shapeParams));
                    logger.info("\n{}", info);
                } catch (NumberOfParametersException ex) {
                    logger.error(() -> NUM_OF_PARAMS_EXCEPTION + ex.getMessage());
                }
            }
            case RECTANGLE -> {
                try {
                    String info = outputRectangle(new Rectangle(shapeParams));
                    logger.info("\n{}", info);
                } catch (NumberOfParametersException ex) {
                    logger.error(() -> NUM_OF_PARAMS_EXCEPTION + ex.getMessage());
                }
            }
            case TRIANGLE -> {
                try {
                    String info = outputTriangle(new Triangle(shapeParams));
                    logger.info("\n{}", info);
                } catch (NumberOfParametersException ex) {
                    logger.error(() -> NUM_OF_PARAMS_EXCEPTION + ex.getMessage());
                } catch (TriangleDoesNotExistException ex) {
                    logger.error(() -> TRIANGLE_DOES_NOT_EXIST_EXCEPTION + ex.getMessage());
                }
            }
            default -> logger.error(() -> SHAPE_SWITCH_DEFAULT + "figure: " + figureType);
        }
    }

    private String outputCommon(Shape shape) {
        return String.format("""
                        Figure type: %s
                        Area: %.2f sqr mm
                        Perimeter: %.2f mm
                        """,
                shape.getName(), shape.getArea(), shape.getPerimeter());
    }

    private String outputCircle(Circle circle) {
        return outputCommon(circle) +
                String.format("""
                                Radius: %.2f mm
                                Diameter: %.2f mm
                                """,
                        circle.getRadius(), circle.gerDiameter());
    }

    private String outputRectangle(Rectangle rectangle) {
        return outputCommon(rectangle) +
                String.format("""
                                Diagonal length: %.2f mm
                                Length: %.2f mm
                                Width: %.2f mm
                                """,
                        rectangle.getDiagonal(), rectangle.getLength(), rectangle.getWidth());
    }

    private String outputTriangle(Triangle triangle) {
        return outputCommon(triangle) +
                String.format("""
                                Alpha angle: %.2f rad
                                Beta angle: %.2f rad
                                Gamma angle: %.2f rad
                                """,
                        triangle.getAlphaAngle(), triangle.getBetaAngle(), triangle.getGammaAngle());
    }
}

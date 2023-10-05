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
    private final String figureType;
    private final double[] shapeParams;
    private static final String SHAPE_SWITCH_DEFAULT = "Unknown shape type";
    private static final String CIRCLE = "CIRCLE";
    private static final String RECTANGLE = "RECTANGLE";
    private static final String TRIANGLE = "TRIANGLE";
    private static final Logger logger = LogManager.getLogger(OutputHandler.class);

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
                    logger.error(ex.getMessage());
                }
            }
            case RECTANGLE -> {
                try {
                    String info = outputRectangle(new Rectangle(shapeParams));
                    logger.info("\n{}", info);
                } catch (NumberOfParametersException ex) {
                    logger.error(ex.getMessage());
                }
            }
            case TRIANGLE -> {
                try {
                    String info = outputTriangle(new Triangle(shapeParams));
                    logger.info("\n{}", info);
                } catch (NumberOfParametersException | TriangleDoesNotExistException ex) {
                    logger.error(ex.getMessage());
                }
            }
            default -> logger.error(SHAPE_SWITCH_DEFAULT);
        }
    }

    private String outputGeneral(Shape shape) {
        return String.format("""
                        Figure type: %s
                        Area: %f sqr mm
                        Perimeter: %f mm
                        """,
                shape.getName(), shape.getArea(), shape.getPerimeter());
    }

    private String outputCircle(Circle circle) {
        return outputGeneral(circle) +
                String.format("""
                                Radius: %f mm
                                Diameter: %f mm
                                """,
                        circle.getRadius(), circle.gerDiameter());
    }

    private String outputRectangle(Rectangle rectangle) {
        return outputGeneral(rectangle) +
                String.format("""
                                Diagonal length: %f mm
                                Length: %f mm
                                Width: %f mm
                                """,
                        rectangle.getDiagonal(), rectangle.getLength(), rectangle.getWidth());
    }

    private String outputTriangle(Triangle triangle) {
        return outputGeneral(triangle) +
                String.format("""
                                Alpha angle: %f rad
                                Beta angle: %f rad
                                Gamma angle: %f rad
                                """,
                        triangle.getAlphaAngle(), triangle.getBetaAngle(), triangle.getGammaAngle());
    }
}

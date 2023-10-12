package ru.cft.focus.geomcalculator.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;
import ru.cft.focus.geomcalculator.exceptions.TriangleDoesNotExistException;
import ru.cft.focus.geomcalculator.shapes.*;

public class OutputHandler {
    private static final String NUM_OF_PARAMS_EXCEPTION = "Invalid number of parameters: ";
    private static final String UNKNOWN_SHAPE_TYPE_EXCEPTION = "Unknown shape type: ";
    private static final Logger logger = LogManager.getLogger(OutputHandler.class);
    private final String figureType;
    private final double[] shapeParams;

    public OutputHandler(String figureType, double[] shapeParams) {
        this.figureType = figureType;
        this.shapeParams = shapeParams;
    }

    public void outputShape() {
        Shape shape;

        try {
            shape = new ShapeFactory().createShape(figureType, shapeParams);
        } catch (NumberOfParametersException ex) {
            logger.error(() -> NUM_OF_PARAMS_EXCEPTION + ex.getMessage());
            return;
        } catch (TriangleDoesNotExistException ex) {
            logger.error(ex.getMessage());
            return;
        } catch (IllegalArgumentException ex) {
            logger.error(() -> UNKNOWN_SHAPE_TYPE_EXCEPTION + "figure: " + figureType);
            return;
        }

        ShapeNames shapeName = ShapeNames.valueOf(figureType);

        switch (shapeName) {
            case CIRCLE -> {
                String info = outputCircle((Circle) shape);
                logger.info("\n{}", info);
            }
            case RECTANGLE -> {
                String info = outputRectangle((Rectangle) shape);
                    logger.info("\n{}", info);
            }
            case TRIANGLE -> {
                String info = outputTriangle((Triangle) shape);
                    logger.info("\n{}", info);
            }
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

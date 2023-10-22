package ru.cft.focus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cft.focus.geomcalculator.exceptions.NumberOfParametersException;
import ru.cft.focus.geomcalculator.exceptions.TriangleDoesNotExistException;
import ru.cft.focus.geomcalculator.handlers.CliHandler;
import ru.cft.focus.geomcalculator.handlers.ShapeDataHandler;
import ru.cft.focus.geomcalculator.shapes.ShapeFactory;
import ru.cft.focus.geomcalculator.shapes.Shape;

public class Main {
    private static final String NUM_OF_PARAMS_EXCEPTION = "Invalid number of parameters: ";
    private static final String UNKNOWN_SHAPE_TYPE_EXCEPTION = "Unknown shape type: ";
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        CliHandler cliHandler = new CliHandler(args);
        String filePath = cliHandler.getInputPath();
//        boolean fileOutput = cliHandler.isFileOutput();
        ShapeDataHandler shapeDataHandler = new ShapeDataHandler(filePath);
        double[] params = shapeDataHandler.getShapeParams();
        String figureType = shapeDataHandler.getFigureType();

        Shape shape;
        try {
            shape = new ShapeFactory().createShape(figureType, params);
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

        String info = shape.printShape();
        logger.info("\n{}", info);
    }

}
package ru.cft.focus;

import ru.cft.focus.geomcalculator.handlers.CliHandler;
import ru.cft.focus.geomcalculator.handlers.OutputHandler;
import ru.cft.focus.geomcalculator.handlers.ShapeDataHandler;

public class Main {
    public static void main(String[] args) {
        CliHandler cliHandler = new CliHandler(args);
        String filePath = cliHandler.getInputPath();
//        boolean fileOutput = cliHandler.isFileOutput();
        ShapeDataHandler shapeDataHandler = new ShapeDataHandler(filePath);
        double[] params = shapeDataHandler.getShapeParams();
        String figureType = shapeDataHandler.getFigureType();

        OutputHandler outputHandler = new OutputHandler(figureType, params);
        outputHandler.outputShape();

    }

}
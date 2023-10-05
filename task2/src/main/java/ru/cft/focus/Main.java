package ru.cft.focus;

import ru.cft.focus.geomcalculator.Geometry;
import ru.cft.focus.geomcalculator.handlers.OutputHandler;

public class Main {
    public static void main(String[] args) {
        Geometry geometry = new Geometry(args);
        double[] params = geometry.getShapeParams();
        String figureType = geometry.getFigureType();

        OutputHandler outputHandler = new OutputHandler(figureType, params);
        outputHandler.outputShape();

    }

}
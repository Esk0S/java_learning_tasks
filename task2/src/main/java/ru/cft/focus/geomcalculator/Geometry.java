package ru.cft.focus.geomcalculator;

import ru.cft.focus.geomcalculator.handlers.CliHandler;
import ru.cft.focus.geomcalculator.handlers.ShapeDataHandler;

public class Geometry {
    private final String figureType;
    private final double[] shapeParams;
    String[] args;

    public Geometry(String[] args) {
        this.args = new String[args.length];
        System.arraycopy(args, 0, this.args, 0, args.length);
        CliHandler cliHandler = new CliHandler(this.args);
        String filePath = cliHandler.getInputPath();
        boolean fileOutput = cliHandler.isFileOutput();
        ShapeDataHandler shapeDataHandler = new ShapeDataHandler(filePath);
        figureType = shapeDataHandler.getFigureType();
        shapeParams = shapeDataHandler.getShapeParams();
    }

    public String getFigureType() {
        return figureType;
    }

    public double[] getShapeParams() {
        return shapeParams;
    }
}

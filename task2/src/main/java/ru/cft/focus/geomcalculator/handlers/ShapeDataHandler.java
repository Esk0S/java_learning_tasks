package ru.cft.focus.geomcalculator.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.io.FileReader;
import java.util.Arrays;

public class ShapeDataHandler {
    private String figureType;
    private double[] shapeParams;
    private static final int DATA_STRINGS_COUNT = 2;
    private static final int SHAPE_TYPE_NUMBER = 0;
    private static final int PARAMS_NUMBER = 1;
    private static final Logger logger = LogManager.getLogger(ShapeDataHandler.class);

    public ShapeDataHandler(String filePath) {
        String[] dataFromFile = openFile(filePath);
        parsShapeDataFromFile(dataFromFile);
    }

    private String[] openFile(String filePath) {

        File file = new File(filePath);
        String[] dataStrings = new String[DATA_STRINGS_COUNT];
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String st;
            int i = 0;
            while ((st = bufferedReader.readLine()) != null && i != DATA_STRINGS_COUNT) {
                dataStrings[i] = st;
                i++;
            }

        } catch (IOException exception) {
            logger.error(exception.getMessage());
        }

        return dataStrings;
    }

    private void parsShapeDataFromFile(String[] data) {
        figureType = data[SHAPE_TYPE_NUMBER];
        String paramsStr = data[PARAMS_NUMBER].trim();
        shapeParams = Arrays.stream(paramsStr.split(" "))
                .mapToDouble(Double::parseDouble).toArray();

    }

    public String getFigureType() {
        return figureType;
    }

    public double[] getShapeParams() {
        return shapeParams;
    }
}

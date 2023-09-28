package ru.cft.focus;

import java.io.PrintWriter;

public class Table {
    private final int tableSize;
    private int maxIndentLength;
    private int indentLength;
    private int tableRowCapacity;
    private final int[][] table;

    public Table(int[][] table) {
        this.tableSize = table.length;
        this.table = new int[tableSize][tableSize];
        for (int i = 0; i < tableSize; i++) {
            System.arraycopy(table[i], 0, this.table[i], 0, tableSize);
        }
    }

    public void printTable() {
        maxIndentLength = Integer.toString(tableSize * tableSize).length();
        indentLength = Integer.toString(tableSize).length();
        tableRowCapacity = indentLength + maxIndentLength * tableSize + tableSize;
        String separatorLine = makeHorizontalSeparatorLine(indentLength, maxIndentLength);
        PrintWriter printWriter = new PrintWriter(System.out);
        for (int i = 0; i <= tableSize; i++) {
            printWriter.println(createTableRow(i));
            printWriter.println(separatorLine);
        }
        printWriter.close();
    }

    private String createTableRow(int i) {
        final String VERTICAL_DELIMITER = "|";
        final String OUTPUT_TYPE = "d";
        final String WHITESPACE = " ";
        StringBuilder numbersLine = new StringBuilder(tableRowCapacity);
        for (int j = 0; j <= tableSize; j++) {
            if (i == 0 && j == 0) {
                numbersLine.append(WHITESPACE.repeat(indentLength));
            } else if (i == 0) {
                numbersLine.append(String.format(VERTICAL_DELIMITER + "%" + maxIndentLength + OUTPUT_TYPE, j));
            } else if (j == 0) {
                numbersLine.append(String.format("%" + indentLength + OUTPUT_TYPE, i));
            } else {
                numbersLine.append(
                        String.format(VERTICAL_DELIMITER + "%" + maxIndentLength + OUTPUT_TYPE, table[i - 1][j - 1]));
            }
        }

        return numbersLine.toString();
    }

    private String makeHorizontalSeparatorLine(int indentLength, int maxIndentLength) {
        final String HORIZONTAL_DELIMITER = "-";
        final String CROSS_DELIMITER = "+";
        StringBuilder line = new StringBuilder(tableRowCapacity);
        line.append(HORIZONTAL_DELIMITER.repeat(indentLength));
        for (int k = 0; k < tableSize; k++) {
            line.append(CROSS_DELIMITER).append(HORIZONTAL_DELIMITER.repeat(maxIndentLength));
        }

        return line.toString();
    }

}

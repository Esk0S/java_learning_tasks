package ru.cft.focus;

import java.io.PrintWriter;

public class Table {
    private final int tableSize;
    private int maxIndentLength;
    private int indentLength;
    private int tableRowCapacity;
    private final int[][] table;
    private static final String HORIZONTAL_DELIMITER = "-";
    private static final String CROSS_DELIMITER = "+";
    private static final String VERTICAL_DELIMITER = "|";
    private static final String OUTPUT_TYPE = "d";
    private static final String WHITESPACE = " ";

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
        String separatorLine = createHorizontalSeparatorLine(indentLength, maxIndentLength);
        PrintWriter printWriter = new PrintWriter(System.out, true);

        printWriter.println(createTableHeader());
        printWriter.println(separatorLine);
        for (int i = 1; i <= tableSize; i++) {
            printWriter.println(createTableRow(i));
            printWriter.println(separatorLine);
        }
    }

    private String createTableHeader() {
        StringBuilder headerBuilder = new StringBuilder(tableRowCapacity);
        headerBuilder.append(WHITESPACE.repeat(indentLength));
        for (int i = 1; i <= tableSize; i++) {
            headerBuilder.append(String.format(VERTICAL_DELIMITER + "%" + maxIndentLength + OUTPUT_TYPE, i));
        }

        return headerBuilder.toString();
    }

    private String createTableRow(int i) {
        StringBuilder numbersLine = new StringBuilder(tableRowCapacity);
        numbersLine.append(String.format("%" + indentLength + OUTPUT_TYPE, i));
        for (int j = 1; j <= tableSize; j++) {
            numbersLine.append(
                    String.format(VERTICAL_DELIMITER + "%" + maxIndentLength + OUTPUT_TYPE, table[i - 1][j - 1]));
        }

        return numbersLine.toString();
    }

    private String createHorizontalSeparatorLine(int indentLength, int maxIndentLength) {
        StringBuilder line = new StringBuilder(tableRowCapacity);
        line.append(HORIZONTAL_DELIMITER.repeat(indentLength));
        for (int k = 0; k < tableSize; k++) {
            line.append(CROSS_DELIMITER).append(HORIZONTAL_DELIMITER.repeat(maxIndentLength));
        }

        return line.toString();
    }

}

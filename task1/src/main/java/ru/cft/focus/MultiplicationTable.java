package ru.cft.focus;

public class MultiplicationTable {
    private final int tableSize;

    public MultiplicationTable(int tableSize) {
        this.tableSize = tableSize;
    }

    public void printTable() {

        int maxIndentLength = Integer.toString(tableSize * tableSize).length();
        int indentLength = Integer.toString(tableSize).length();
        String line = makeLine(indentLength, maxIndentLength);
        for (int i = 0; i <= tableSize; i++) {
            for (int j = 0; j <= tableSize; j++) {
                new FormattedOutput(indentLength, maxIndentLength).formatAndPrint(i, j);
            }
            System.out.println();
            System.out.println(line);
        }

    }

    private String makeLine(int indentLength, int maxIndentLength) {
        StringBuilder line = new StringBuilder();
        line.append("-".repeat(indentLength));
        for (int k = 0; k < tableSize; k++) {
            line.append("+").append("-".repeat(maxIndentLength));
        }
        return line.toString();
    }
}

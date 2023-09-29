package ru.cft.focus;

public class Main {
    private static final int UPPER_BOUND = 32;
    private static final int LOWER_BOUND = 1;

    public static void main(String[] args) {

        int tableSize = TableSizeReader.readInputTableSize(LOWER_BOUND, UPPER_BOUND);
        int[][] multiplicationTable = MultiplicationTable.createMultiplicationTableArray(tableSize);
        Table table = new Table(multiplicationTable);
        table.printTable();
    }
}
package ru.cft.focus;

public class Main {

    public static void main(String[] args) {
        int upperBound = 32;
        int lowerBound = 1;
        int tableSize = TableSizeReader.readInputTableSize(lowerBound, upperBound);
        int[][] multiplicationTable = MultiplicationTable.createMultiplicationTableArray(tableSize);
        Table table = new Table(multiplicationTable);
        table.printTable();
    }
}
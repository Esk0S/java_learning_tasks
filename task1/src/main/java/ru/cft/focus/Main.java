package ru.cft.focus;

public class Main {
    public static void main(String[] args) {
        Input in = new Input(1, 32);
        int tableSize = in.inputTableSize();
        MultiplicationTable multiplicationTable = new MultiplicationTable(tableSize);
        multiplicationTable.printTable();
    }
}
package ru.cft.focus;

public final class MultiplicationTable {
    private MultiplicationTable() {
    }

    public static int[][] createMultiplicationTableArray(int tableSize) {
        int[][] table = new int[tableSize][tableSize];
        for (int i = 1; i <= tableSize; i++) {
            for (int j = 1; j <= tableSize; j++) {
                table[i - 1][j - 1] = i * j;
            }
        }

        return table;
    }
}

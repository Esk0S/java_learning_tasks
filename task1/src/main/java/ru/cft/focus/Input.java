package ru.cft.focus;

import java.util.Scanner;

public class Input {
    private final int minBound;
    private final int maxBound;

    public Input(int minBound, int maxBound) {
        this.minBound = minBound;
        this.maxBound = maxBound;
    }

    public int inputTableSize() {
        Scanner sc = new Scanner(System.in);
        int tableSize;
        while (true) {
            System.out.println("Enter table size: ");
            String strTableSize = sc.nextLine();
            try {
                tableSize = Integer.parseInt(strTableSize);
                if (tableSize < minBound || tableSize > maxBound) {
                    continue;
                }
                break;
            } catch (NumberFormatException ignored) {

            }
        }
        return tableSize;

    }
}

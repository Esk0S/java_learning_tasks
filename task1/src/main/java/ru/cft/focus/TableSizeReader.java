package ru.cft.focus;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class TableSizeReader {
    private TableSizeReader() {
    }

    public static int readInputTableSize(int minBound, int maxBound) {
        Scanner sc = new Scanner(System.in);
        int tableSize;
        final String QUERY_TABLE_SIZE_MESSAGE = "Enter table size: ";
        final String ERROR_MESSAGE = "Parse error: ";

        while (true) {
            System.out.println(QUERY_TABLE_SIZE_MESSAGE);
            try {
                tableSize = sc.nextInt();
                if (tableSize < minBound || tableSize > maxBound) {
                    continue;
                }
            } catch (InputMismatchException exception) {
                sc.next();
                System.err.println(ERROR_MESSAGE + exception.getMessage());
                continue;
            }
            sc.close();
            return tableSize;
        }

    }
}

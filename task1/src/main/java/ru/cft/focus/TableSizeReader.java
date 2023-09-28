package ru.cft.focus;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class TableSizeReader {
    private static final String QUERY_TABLE_SIZE_MESSAGE = "Enter table size: ";
    private static final String ERROR_MESSAGE = "Parse error: ";

    private TableSizeReader() {
    }

    public static int readInputTableSize(int minBound, int maxBound) {
        Scanner scanner = new Scanner(System.in);
        int tableSize;

        while (true) {
            System.out.println(QUERY_TABLE_SIZE_MESSAGE);
            try {
                tableSize = scanner.nextInt();
                if (tableSize < minBound || tableSize > maxBound) {
                    continue;
                }
            } catch (InputMismatchException exception) {
                scanner.next();
                System.err.println(ERROR_MESSAGE + exception.getMessage());
                continue;
            }
            return tableSize;
        }

    }
}

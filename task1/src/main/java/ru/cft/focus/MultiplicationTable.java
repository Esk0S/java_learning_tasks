package ru.cft.focus;

import java.util.Scanner;

public class MultiplicationTable {
    private int size;

    public void input() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter table size: ");

        while (true) {
            String sizeStr = sc.nextLine();
            try {
                size = Integer.parseInt(sizeStr);
                if (size < 1 || size > 32) {
                    System.out.println("Enter table size between 1 and 32: ");
                    continue;
                }
                break;
            } catch (NumberFormatException ex) {
                if (!sizeStr.strip().equals("q")) {
                    System.out.println("Enter the correct table size: ");
                } else
                    System.exit(0);
            }
        }
        printTable();
    }

    public void printTable() {
        int ws = Integer.toString(size * size).length(); // ws is whitespaces
        int wsSize = Integer.toString(size).length();
        for (int i = 0; i <= size; i++) {
            for (int j = 0; j <= size; j++) {
                if (i == 0 && j == 0)
                    System.out.printf("%" + wsSize + "s", "");
                else if (i == 0) {
                    System.out.printf("|" + "%"+ws+"d", j);
                } else if (j == 0) {
                    System.out.printf("%"+wsSize+"d", i);
                } else {
                    System.out.printf("|" + "%"+ws+"d", j * i);
                }
            }
            System.out.println();
            for (int k = 0; k <= size; k++) {
                if (i == 0 && k == 0)
                    System.out.print("-".repeat(wsSize));
                else if (k == 0 ) {
                    System.out.printf("-".repeat(wsSize));
                }
                else {
                    System.out.printf("+" + "-".repeat(ws));
                }
            }
            System.out.println();

        }
    }
}

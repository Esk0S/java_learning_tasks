package ru.cft.focus;

public class FormattedOutput {
    private final int indentLength;
    private final int maxIndentLength;

    public FormattedOutput(int indentLength, int maxIndentLength) {
        this.indentLength = indentLength;
        this.maxIndentLength = maxIndentLength;
    }

    public void formatAndPrint(int i, int j) {
        if (i == 0 && j == 0) {
            System.out.printf("%" + indentLength + "s", "");
        } else if (i == 0) {
            System.out.printf("|" + "%" + maxIndentLength + "d", j);
        } else if (j == 0) {
            System.out.printf("%" + indentLength + "d", i);
        } else {
            System.out.printf("|" + "%" + maxIndentLength + "d", Multiplier.multiply(j, i));
        }
    }
}

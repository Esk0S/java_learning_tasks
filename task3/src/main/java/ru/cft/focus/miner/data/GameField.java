package ru.cft.focus.miner.data;

import lombok.Getter;
import ru.cft.focus.miner.model.Cell;
import ru.cft.focus.miner.model.GameType;

public class GameField {
    @Getter
    private int bombsCount;
    @Getter
    private int rowsCount;
    @Getter
    private int colsCount;
    @Getter
    private int markCount;
    @Getter
    private int needToOpenCellsToWin;
    @Getter
    private int openedCellsCount;
    @Getter
    private GameType gameType;
    private Cell[][] cell;

    public GameField() {
    }

    public void initialize(int bombsCount, int rowsCount, int colsCount, GameType gameType) {
        this.bombsCount = bombsCount;
        this.rowsCount = rowsCount;
        this.colsCount = colsCount;
        this.gameType = gameType;
        this.markCount = 0;
        needToOpenCellsToWin = rowsCount * colsCount - bombsCount;
        openedCellsCount = 0;

        cell = new Cell[getColsCount()][getRowsCount()];
        for (int i = 0; i < getColsCount(); i++) {
            for (int j = 0; j < getRowsCount(); j++) {
                cell[i][j] = new Cell();
            }
        }
    }

    public void setBombOnField(int x, int y) {
        cell[x][y].setBomb(true);
    }

    public boolean checkBombOnField(int x, int y) {
        return cell[x][y].hasBomb();
    }

    public void incMarkCount() {
        markCount++;
    }

    public void decreaseMarkCount() {
        markCount--;
    }

    public void incOpenedCellsCountIfNotOpened(int x, int y) {
        if (!isCellOpened(x, y)) {
            openedCellsCount++;
        }
    }

    public Cell getCell(int x, int y) {
        return cell[x][y];
    }

    public boolean isCellOpened(int x, int y) {
        return cell[x][y].isOpened();
    }

    public int getEstimateBombCount() {
        return getBombsCount() - getMarkCount();
    }

}

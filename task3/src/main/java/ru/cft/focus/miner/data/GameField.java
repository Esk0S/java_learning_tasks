package ru.cft.focus.miner.data;

import lombok.Getter;
import ru.cft.focus.miner.model.Cell;
import ru.cft.focus.miner.model.CellState;
import ru.cft.focus.miner.model.GameType;

import java.util.ArrayList;
import java.util.List;

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
    private List<int[]> bombPositions;
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
        bombPositions = new ArrayList<>();
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

    public void markCell(int x, int y, boolean mark) {
        cell[x][y].setMarked(mark);
    }

    public boolean isMarked(int x, int y) {
        return cell[x][y].isMarked();
    }

    public void incMarkCount() {
        markCount++;
    }

    public void decreaseMarkCount() {
        markCount--;
    }

    public void setCellState(int x, int y, CellState cellState) {
        cell[x][y].setCellState(cellState);
    }

    public CellState getCellState(int x, int y) {
        return cell[x][y].getCellState();
    }

    public void addBombPosition(int x, int y) {
        bombPositions.add(new int[]{x, y});
    }

    public void incOpenedCellsCountIfNotOpened(int x, int y) {
        if (!isCellOpened(x, y)) {
            openedCellsCount++;
        }
    }

    public void setCellOpened(int x, int y) {
        cell[x][y].setOpened();
    }

    public boolean isCellOpened(int x, int y) {
        return cell[x][y].isOpened();
    }

    public int getEstimateBombCount() {
        return getBombsCount() - getMarkCount();
    }

}

package ru.cft.focus.miner.data;

import lombok.Getter;
import ru.cft.focus.miner.model.Cell;
import ru.cft.focus.miner.model.CellState;
import ru.cft.focus.miner.model.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class GameValues {
    @Getter
    private static int bombsCount;
    @Getter
    private static int timerValue;
    @Getter
    private static int rowsCount;
    @Getter
    private static int colsCount;
    @Getter
    private static int markCount;
    @Getter
    private static List<int[]> bombPositions;
    @Getter
    private static int needToOpenCellsToWin;
    @Getter
    private static int openedCellsCount;
    @Getter
    private static GameType gameType;
    private static Cell[][] cell;
    private static GameTimer gameTimer;

    private GameValues() {
    }

    public static void initialize(int bombsCount, int rowsCount, int colsCount, GameType gameType) {
        GameValues.bombsCount = bombsCount;
        createTimer();
        resetTimerValue();
        GameValues.rowsCount = rowsCount;
        GameValues.colsCount = colsCount;
        GameValues.gameType = gameType;
        resetMarkCount();
        bombPositions = new ArrayList<>();
        needToOpenCellsToWin = rowsCount * colsCount - bombsCount;
        openedCellsCount = 0;

        cell = new Cell[getColsCount()][getRowsCount()];
        for (int i = 0; i < getColsCount(); i++) {
            for (int j = 0; j < getRowsCount(); j++) {
                cell[i][j] = new Cell();
                cell[i][j].setCanBeMarked(true);
            }
        }
    }

    public static void incTimerValue() {
        GameValues.timerValue++;
    }

    public static void setBombOnField(int x, int y) {
        cell[x][y].setBomb(true);
    }

    public static boolean checkBombOnField(int x, int y) {
        return cell[x][y].hasBomb();
    }

    public static boolean checkIfCellCanBeMarked(int x, int y) {
        return cell[x][y].isCanBeMarked();
    }

    public static void setCellAsCanNotBeMarked(int x, int y) {
        cell[x][y].setCanBeMarked(false);
    }

    public static void setCellAsMarked(int x, int y, boolean mark) {
        cell[x][y].setMarked(mark);
    }

    public static boolean isMarked(int x, int y) {
        return cell[x][y].isMarked();
    }

    private static void createTimer() {
        stopTimer();
        gameTimer = new GameTimer();
    }

    public static void startTimer(TimerTask timerTask) {
        if (gameTimer == null) {
            createTimer();
        }
        gameTimer.setTimer(timerTask);
    }

    public static void stopTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer = null;
        }
    }

    private static void resetMarkCount() {
        markCount = 0;
    }

    private static void resetTimerValue() {
        timerValue = 0;
    }

    public static void incMarkCount() {
        markCount++;
    }

    public static void decreaseMarkCount() {
        markCount--;
    }

    public static void setCellState(int x, int y, CellState cellState) {
        cell[x][y].setCellState(cellState);
    }

    public static CellState getCellState(int x, int y) {
        return cell[x][y].getCellState();
    }

    public static void setCellVisited(int x, int y, boolean visited) {
        cell[x][y].setVisited(visited);
    }

    public static boolean isCellVisited(int x, int y) {
        return cell[x][y].isVisited();
    }

    public static void addBombPosition(int x, int y) {
        bombPositions.add(new int[]{x, y});
    }

    public static void incOpenedCellsCountIfNotOpened(int x, int y) {
        if (isCellClosed(x, y)) {
            openedCellsCount++;
        }
    }

    public static void setCellOpened(int x, int y) {
        cell[x][y].setOpened();
    }

    public static boolean isCellClosed(int x, int y) {
        return !cell[x][y].isOpened();
    }
}

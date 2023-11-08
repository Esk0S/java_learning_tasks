package ru.cft.focus.miner.controller;

import ru.cft.focus.miner.model.*;
import ru.cft.focus.miner.view.MainWindow;
import ru.cft.focus.miner.view.SettingsWindow;
import ru.cft.focus.miner.view.View;

import java.util.List;

import static ru.cft.focus.miner.data.GameValues.*;

public class GameController {
    private final GameModel gameModel;
    private final MainWindow mainWindow;
    private final SettingsWindow settingsWindow;
    private final View view;

    public GameController(MainWindow mainWindow, SettingsWindow settingsWindow, GameModel gameModel, View view) {
        this.mainWindow = mainWindow;
        this.settingsWindow = settingsWindow;
        this.gameModel = gameModel;
        this.view = view;
    }

    public void startNewGame(int bombs, int rows, int cols, GameType gameType) {
        gameModel.addCellListener(view);
        gameModel.addNewGameListener(view);
        gameModel.addWinListener(view);
        gameModel.addLoseListener(view);
        gameModel.addRecordListener(view);

        mainWindow.setCellListener((x, y, buttonType) -> {
            switch (buttonType) {
                case LEFT_BUTTON -> {
                    var cells = gameModel.getOpenedCells(x, y);
                    openCells(bombs, rows, cols, cells);
                }
                case RIGHT_BUTTON -> setMark(x, y);
                case MIDDLE_BUTTON -> {
                    var cells = gameModel.getCellsAround(x, y);
                    openCells(bombs, rows, cols, cells);
                }
            }
        });
        gameModel.notifyNewGameListeners(gameModel.startNewGame(bombs, rows, cols, gameType));

        setGameType();
    }

    private void setMark(int x, int y) {
        CellEvent cell = gameModel.getCellForMark(x, y);
        if (cell != null) {
            gameModel.notifyCellListeners(cell);
        }
    }

    private void openCells(int bombs, int rows, int cols, List<CellEvent> cells) {
        boolean bomb = false;
        for (var cell : cells) {
            if (cell.getState() == CellState.BOMB) {
                bomb = true;
            }
            gameModel.notifyCellListeners(cell);
        }
        if (bomb) {
            gameModel.notifyLoseListeners(new LoseEvent(getBombPositions(), bombs, rows, cols, getGameType()));
        }
        if (getOpenedCellsCount() == getNeedToOpenCellsToWin()) {
            stopTimer();
            gameModel.notifyWinListeners(new WinEvent(bombs, rows, cols, getTimerValue(), getGameType()));
        }
    }

    private void setGameType() {
        settingsWindow.setGameTypeListener(gameType -> {
            int bombsCount = 0, rowsCount = 0, colsCount = 0;
            GameType gameTypeValue = GameType.valueOf(gameType.name());
            switch (gameType) {
                case NOVICE -> {
                    bombsCount = 10;
                    rowsCount = 10;
                    colsCount = 10;
                }
                case MEDIUM -> {
                    bombsCount = 40;
                    rowsCount = 16;
                    colsCount = 16;
                }
                case EXPERT -> {
                    bombsCount = 99;
                    rowsCount = 16;
                    colsCount = 30;
                }
            }

            startNewGame(bombsCount, rowsCount, colsCount, gameTypeValue);
        });
    }


}

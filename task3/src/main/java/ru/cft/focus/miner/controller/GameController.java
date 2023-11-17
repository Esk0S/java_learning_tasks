package ru.cft.focus.miner.controller;

import ru.cft.focus.miner.model.*;
import ru.cft.focus.miner.model.GameType;
import ru.cft.focus.miner.view.*;

public class GameController implements GameTypeListener, CellEventListener {
    private final GameModel gameModel;

    public GameController(MainWindow mainWindow, SettingsWindow settingsWindow, GameModel gameModel) {
        this.gameModel = gameModel;

        gameModel.notifyHighScoresWindowListeners();
        settingsWindow.setGameTypeListener(this);
        mainWindow.setCellListener(this);
    }

    public void startNewGame(int bombs, int rows, int cols, GameType gameType) {
        gameModel.startNewGame(bombs, rows, cols, gameType);
        gameModel.notifyNewGameListeners(new NewGameEvent(bombs, rows, cols, gameType));
    }

    @Override
    public void onMouseClick(int x, int y, ButtonType buttonType) {
        switch (buttonType) {
            case LEFT_BUTTON -> gameModel.openCells(x, y);
            case RIGHT_BUTTON -> gameModel.markCell(x, y);
            case MIDDLE_BUTTON -> gameModel.openCellsAround(x, y);
        }
    }

    @Override
    public void onGameTypeChanged(ru.cft.focus.miner.view.GameType gameType) {
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
    }
}

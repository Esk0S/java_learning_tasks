package ru.cft.focus.miner.view;

import ru.cft.focus.miner.data.GameField;
import ru.cft.focus.miner.data.GameTimer;
import ru.cft.focus.miner.data.GameTimerTask;
import ru.cft.focus.miner.model.*;
import ru.cft.focus.miner.model.GameType;

public class View implements CellListener, NewGameListener, WinListener, LoseListener, RecordListener {
    private boolean start;
    private final MainWindow mainWindow;
    private final GameModel gameModel;
    private final GameField gameField;
    private GameTimer gameTimer;

    public View(GameModel gameModel, GameField gameField, MainWindow mainWindow) {
        this.gameModel = gameModel;
        this.mainWindow = mainWindow;
        this.gameField = gameField;
        start = true;
    }

    @Override
    public void onCellAction(CellEvent cellEvent) {
        int x = cellEvent.getX();
        int y = cellEvent.getY();

        CellState cellState = cellEvent.getState();
        ActionType actionType = cellEvent.getButtonType();

        switch (actionType) {
            case OPEN_CELLS -> openCells(x, y, cellState);
            case MARK_UNMARK -> markUnmark(x, y, cellState);
            case OPEN_CELLS_AROUND -> openCellsAround(x, y, cellState);
        }
    }

    @Override
    public void onNewGame(NewGameEvent event) {
        gameModel.startNewGame(event.getBombs(), event.getRows(), event.getCols(), event.getGameType());
        stopTimer();
        start = true;
        mainWindow.createGameField(event.getRows(), event.getCols());
        mainWindow.setBombsCount(event.getBombs());
        mainWindow.setTimerValue(0);
    }

    @Override
    public void onWin() {
        gameTimer.cancel();
        gameModel.notifyRecordListeners();
        int bombs = gameField.getBombsCount();
        int rows = gameField.getRowsCount();
        int cols = gameField.getColsCount();
        GameType gameType = gameField.getGameType();
        new WinWindow(mainWindow,
                e -> gameModel.notifyNewGameListeners(new NewGameEvent(bombs, rows, cols, gameType)),
                e -> System.exit(0));
    }

    @Override
    public void onLose() {
        stopTimer();
        var bombPositions = gameField.getBombPositions();
        int bombs = gameField.getBombsCount();
        int rows = gameField.getRowsCount();
        int cols = gameField.getColsCount();
        GameType gameType = gameField.getGameType();
        for (var i : bombPositions) {
            mainWindow.setCellImage(i[0], i[1], GameImage.BOMB);
        }
        new LoseWindow(mainWindow,
                e -> gameModel.notifyNewGameListeners(new NewGameEvent(bombs, rows, cols, gameType)),
                e -> System.exit(0));
    }

    @Override
    public void onRecord() {
        int recordValue = gameModel.readRecord(gameField.getGameType());
        int currentResult = gameTimer.getTimerValue();
        GameType gameType = gameField.getGameType();
        if (currentResult < recordValue) {
            new RecordsWindow(mainWindow,
                    name -> gameModel.updateRecord(name, currentResult, gameType));
        }
    }

    private void updateCellView(int x, int y, CellState cellState) {
        switch (cellState) {
            case NUM_1 -> mainWindow.setCellImage(x, y, GameImage.NUM_1);
            case NUM_2 -> mainWindow.setCellImage(x, y, GameImage.NUM_2);
            case NUM_3 -> mainWindow.setCellImage(x, y, GameImage.NUM_3);
            case NUM_4 -> mainWindow.setCellImage(x, y, GameImage.NUM_4);
            case NUM_5 -> mainWindow.setCellImage(x, y, GameImage.NUM_5);
            case NUM_6 -> mainWindow.setCellImage(x, y, GameImage.NUM_6);
            case NUM_7 -> mainWindow.setCellImage(x, y, GameImage.NUM_7);
            case NUM_8 -> mainWindow.setCellImage(x, y, GameImage.NUM_8);
            case BOMB -> mainWindow.setCellImage(x, y, GameImage.BOMB);

            default -> mainWindow.setCellImage(x, y, GameImage.EMPTY);
        }
    }

    private void updateMarksView(int x, int y, CellState cellState) {
        if (cellState != CellState.NONE) {
            mainWindow.setCellImage(x, y, GameImage.valueOf(cellState.name()));
            mainWindow.setBombsCount(gameField.getEstimateBombCount());
        }
    }

    private void openCells(int x, int y, CellState cellState) {
        if (start) {
            startTimerTask();
            start = false;
        }
        updateCellView(x, y, cellState);
    }

    private void markUnmark(int x, int y, CellState cellState) {
        updateMarksView(x, y, cellState);
    }

    private void openCellsAround(int x, int y, CellState cellState) {
        updateCellView(x, y, cellState);
    }

    private void startTimerTask() {
        startTimer(new GameTimerTask(mainWindow));
    }

    private void createTimer() {
        stopTimer();
        gameTimer = new GameTimer();
    }

    public void startTimer(GameTimerTask timerTask) {
        if (gameTimer == null) {
            createTimer();
        }
        gameTimer.setTimer(timerTask);
    }

    public void stopTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer = null;
        }
    }

}

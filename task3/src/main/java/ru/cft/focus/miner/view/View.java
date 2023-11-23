package ru.cft.focus.miner.view;

import ru.cft.focus.miner.controller.GameController;
import ru.cft.focus.miner.data.GameField;
import ru.cft.focus.miner.data.GameTimer;
import ru.cft.focus.miner.data.GameTimerTask;
import ru.cft.focus.miner.model.*;

import java.util.ArrayList;
import java.util.Map;

public class View implements CellListener, NewGameListener, GameWonListener, GameLostListener, RecordListener, HighScoresWindowListener {
    private boolean start;
    private final MainWindow mainWindow;
    private final GameModel gameModel;
    private final GameField gameField;
    private final GameController gameController;
    private final HighScoresWindow highScoresWindow;
    private GameTimer gameTimer;
    static GameImage[] openCellImages = {
            GameImage.EMPTY,
            GameImage.NUM_1,
            GameImage.NUM_2,
            GameImage.NUM_3,
            GameImage.NUM_4,
            GameImage.NUM_5,
            GameImage.NUM_6,
            GameImage.NUM_7,
            GameImage.NUM_8,
    };

    public View(GameModel gameModel, GameField gameField, MainWindow mainWindow, HighScoresWindow highScoresWindow, GameController gameController) {
        this.gameModel = gameModel;
        this.mainWindow = mainWindow;
        this.gameField = gameField;
        this.highScoresWindow = highScoresWindow;
        this.gameController = gameController;
        start = true;

        gameModel.addHighRecordListener(this);
        gameModel.addCellListener(this);
        gameModel.addNewGameListener(this);
        gameModel.addWinListener(this);
        gameModel.addLoseListener(this);
        gameModel.addRecordListener(this);
    }

    @Override
    public void onCellAction(CellEvent cellEvent) {
        int x = cellEvent.getX();
        int y = cellEvent.getY();
        Cell cell = cellEvent.getCell();

        ActionType actionType = cellEvent.getButtonType();

        switch (actionType) {
            case OPEN_CELLS -> openCells(x, y, cell);
            case MARK_UNMARK -> updateMarksView(x, y, cell);
            case OPEN_CELLS_AROUND -> openCellsAround(x, y, cell);
        }
    }

    @Override
    public void onNewGame(NewGameEvent event) {
        stopTimer();
        start = true;
        mainWindow.createGameField(event.getRows(), event.getCols());
        mainWindow.setBombsCount(event.getBombs());
        mainWindow.setTimerValue(0);
    }

    @Override
    public void onGameWon() {
        gameTimer.cancel();
        gameModel.notifyRecordListeners();
        int bombs = gameField.getBombsCount();
        int rows = gameField.getRowsCount();
        int cols = gameField.getColsCount();
        new WinWindow(mainWindow,
                e -> gameController.startNewGame(bombs, rows, cols, gameField.getGameType()),
//                        gameModel.notifyNewGameListeners(new NewGameEvent(bombs, rows, cols, gameField.getGameType())),
                e -> System.exit(0));
    }

    @Override
    public void onGameLost() {
        stopTimer();
        var bombPositions = new ArrayList<int[]>(gameField.getBombsCount());
        for (int i = 0; i < gameField.getColsCount(); i++) {
            for (int j = 0; j < gameField.getRowsCount(); j++) {
                if (gameField.checkBombOnField(i, j)) {
                    bombPositions.add(new int[]{i, j});
                }
            }
        }

        int bombs = gameField.getBombsCount();
        int rows = gameField.getRowsCount();
        int cols = gameField.getColsCount();
        for (var i : bombPositions) {
            mainWindow.setCellImage(i[0], i[1], GameImage.BOMB);
        }
        new LoseWindow(mainWindow,
                e -> gameController.startNewGame(bombs, rows, cols, gameField.getGameType()),
//                        gameModel.notifyNewGameListeners(new NewGameEvent(bombs, rows, cols, gameField.getGameType())),
                e -> System.exit(0));
    }

    @Override
    public void onRecord() {
        int recordValue = gameModel.readRecord(gameField.getGameType());
        int currentResult = gameTimer.getTimerValue();
        if (currentResult < recordValue) {
            new RecordsWindow(mainWindow,
                    name -> gameModel.updateHighScoresFile(name, currentResult, gameField.getGameType()));
            gameModel.notifyHighScoresWindowListeners();
        }
    }

    @Override
    public void updateHighScoresWindow() {
        Map<String, Map<String, Integer>> records = gameModel.parseRecordsFromFile();
        for (var gameType : records.entrySet()) {
            for (var recordEntry : gameType.getValue().entrySet()) {
                String playerName = recordEntry.getKey();
                int playerRecord = recordEntry.getValue();
                switch (GameType.valueOf(gameType.getKey())) {
                    case NOVICE -> highScoresWindow.setNoviceRecord(playerName, playerRecord);
                    case MEDIUM -> highScoresWindow.setMediumRecord(playerName, playerRecord);
                    case EXPERT -> highScoresWindow.setExpertRecord(playerName, playerRecord);
                }
            }
        }
    }

    private void updateCellView(int x, int y, Cell cell) {
        mainWindow.setCellImage(x, y, openCellImages[cell.getBombsAround()]);
    }

    private void updateMarksView(int x, int y, Cell cell) {
        if (cell.isMarked()) {
            mainWindow.setCellImage(x, y, GameImage.MARKED);
        } else if (!cell.isOpened()) {
            mainWindow.setCellImage(x, y, GameImage.CLOSED);
        }
        mainWindow.setBombsCount(gameField.getEstimateBombCount());
    }

    private void openCells(int x, int y, Cell cell) {
        if (start) {
            startTimerTask();
            start = false;
        }
        updateCellView(x, y, cell);
    }

    private void openCellsAround(int x, int y, Cell cell) {
        updateCellView(x, y, cell);
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

package ru.cft.focus.miner.view;

import ru.cft.focus.miner.model.*;
import ru.cft.focus.miner.model.GameType;

import java.util.TimerTask;

import static ru.cft.focus.miner.data.GameValues.*;

public class View implements CellListener, NewGameListener, WinListener, LoseListener, RecordListener {
    private boolean start;
    private final MainWindow mainWindow;
    private final GameModel gameModel;
    private final TimerTask timerTask;

    public View(GameModel gameModel, MainWindow mainWindow, TimerTask timerTask) {
        this.gameModel = gameModel;
        this.mainWindow = mainWindow;
        this.timerTask = timerTask;
        start = true;
    }

    @Override
    public void onClick(CellEvent cellEvent) {
        int x = cellEvent.getX();
        int y = cellEvent.getY();

        CellState cellState = cellEvent.getState();
        ButtonTypeModel buttonType = cellEvent.getButtonType();
        switch (buttonType) {
            case LEFT_BUTTON -> {
                if (start) {
                    TimerTask copyTask = new TimerTask() {
                        @Override
                        public void run() {
                            timerTask.run();
                        }
                    };
                    startTimer(copyTask);
                    start = false;
                }
                updateCellView(x, y, cellState);
            }
            case RIGHT_BUTTON -> updateMarksView(x, y, cellState, cellEvent.getEstimateBombCount());
            case MIDDLE_BUTTON -> updateCellView(x, y, cellState);
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
    public void onWin(WinEvent event) {
        NewGameEvent newGameEvent = gameModel.startNewGame(event.getBombs(), event.getRows(), event.getCols(), event.getGameType());

        gameModel.notifyRecordListeners(new RecordEvent(event.getGameType(), event.getTime()));
        new WinWindow(mainWindow,
                e -> gameModel.notifyNewGameListeners(newGameEvent),
                e -> System.exit(0));
    }

    @Override
    public void onLose(LoseEvent event) {
        for (var i : event.getBombPositions()) {
            mainWindow.setCellImage(i[0], i[1], GameImage.BOMB);
        }
        NewGameEvent newGameEvent = gameModel.startNewGame(event.getBombs(), event.getRows(),
                event.getCols(), event.getGameType());
        new LoseWindow(mainWindow,
                e -> gameModel.notifyNewGameListeners(newGameEvent),
                e -> System.exit(0));
    }

    @Override
    public void onRecord(RecordEvent event) {
        int recordValue = gameModel.readRecord(event.getGameType());
        int currentResult = event.getTime();
        GameType gameType = event.getGameType();
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

    private void updateMarksView(int x, int y, CellState cellState, int estimateBombCount) {
        mainWindow.setCellImage(x, y, GameImage.valueOf(cellState.name()));
        mainWindow.setBombsCount(estimateBombCount);
    }

}

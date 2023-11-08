package ru.cft.focus.miner.app;

import lombok.extern.log4j.Log4j2;
import ru.cft.focus.miner.controller.GameController;
import ru.cft.focus.miner.data.GameValues;
import ru.cft.focus.miner.model.*;
import ru.cft.focus.miner.model.GameType;
import ru.cft.focus.miner.view.*;

import java.util.TimerTask;

@Log4j2
public class Application {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        SettingsWindow settingsWindow = new SettingsWindow(mainWindow);
        HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
        GameModel gameModel = new GameModel();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                GameValues.incTimerValue();
                mainWindow.setTimerValue(GameValues.getTimerValue());
            }
        };
        View view = new View(gameModel, mainWindow, timerTask);

        GameController gameController = new GameController(mainWindow, settingsWindow, gameModel, view);

        gameController.startNewGame(10, 10, 10, GameType.NOVICE);

        mainWindow.setNewGameMenuAction(e ->
                gameController.startNewGame(GameValues.getBombsCount(), GameValues.getRowsCount(),
                        GameValues.getColsCount(), GameValues.getGameType()));


        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));
        mainWindow.setExitMenuAction(e -> mainWindow.dispose());

        mainWindow.setVisible(true);

    }
}

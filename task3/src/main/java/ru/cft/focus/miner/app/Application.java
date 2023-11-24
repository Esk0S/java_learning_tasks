package ru.cft.focus.miner.app;

import lombok.extern.log4j.Log4j2;
import ru.cft.focus.miner.controller.GameController;
import ru.cft.focus.miner.data.GameField;
import ru.cft.focus.miner.model.*;
import ru.cft.focus.miner.model.GameType;
import ru.cft.focus.miner.view.*;

@Log4j2
public class Application {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        SettingsWindow settingsWindow = new SettingsWindow(mainWindow);
        HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
        GameField gameField = new GameField();
        GameModel gameModel = new GameModel(gameField);

        GameController gameController = new GameController(mainWindow, settingsWindow, gameModel);

        View view = new View(gameModel, gameField, mainWindow, highScoresWindow, gameController);

        gameController.startNewGame(10, 10, 10, GameType.NOVICE);

        mainWindow.setNewGameMenuAction(e ->
                gameController.startNewGame(gameField.getBombsCount(), gameField.getRowsCount(),
                        gameField.getColsCount(), gameField.getGameType()));

        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));
        mainWindow.setExitMenuAction(e -> mainWindow.dispose());

        mainWindow.setVisible(true);

    }
}

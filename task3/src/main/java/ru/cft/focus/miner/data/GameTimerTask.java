package ru.cft.focus.miner.data;

import ru.cft.focus.miner.view.MainWindow;

import java.util.TimerTask;

public class GameTimerTask extends TimerTask {
    private int timerValue = 0;
    private final MainWindow mainWindow;

    public GameTimerTask(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void run() {
        timerValue++;
        mainWindow.setTimerValue(timerValue);
    }

    public int getTimerValue() {
        return timerValue;
    }
}

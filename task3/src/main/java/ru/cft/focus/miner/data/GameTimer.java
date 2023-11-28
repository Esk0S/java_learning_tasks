package ru.cft.focus.miner.data;

import java.util.Timer;

public class GameTimer extends Timer {
    private GameTimerTask timerTask;

    public void setTimer(GameTimerTask timerTask) {
        this.timerTask = timerTask;
        scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    public int getTimerValue() {
        return timerTask.getTimerValue();
    }
}

package ru.cft.focus.miner.data;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer extends Timer {
    public void setTimer(TimerTask timerTask) {
        scheduleAtFixedRate(timerTask, 1000, 1000);
    }
}

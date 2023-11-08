package ru.cft.focus.miner.model;

import lombok.Getter;

@Getter
public class RecordEvent {
    private final GameType gameType;
    private final int time;

    public RecordEvent(GameType gameType, int time) {
        this.gameType = gameType;
        this.time = time;
    }

}

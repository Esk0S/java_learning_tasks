package ru.cft.focus.miner.model;

import lombok.Getter;

@Getter
public class WonEvent {
    private final int bombs;
    private final int rows;
    private final int cols;
    private final GameType gameType;

    public WonEvent(int bombs, int rows, int cols, GameType gameType) {
        this.bombs = bombs;
        this.rows = rows;
        this.cols = cols;
        this.gameType = gameType;
    }

}

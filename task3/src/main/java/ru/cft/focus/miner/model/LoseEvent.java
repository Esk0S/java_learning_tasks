package ru.cft.focus.miner.model;

import lombok.Getter;

import java.util.List;

@Getter
public class LoseEvent {
    private final List<int[]> bombPositions;
    private final int bombs;
    private final int rows;
    private final int cols;
    private final GameType gameType;

    public LoseEvent(List<int[]> bombPositions, int bombs, int rows, int cols, GameType gameType) {
        this.bombPositions = bombPositions;
        this.bombs = bombs;
        this.rows = rows;
        this.cols = cols;
        this.gameType = gameType;
    }

}

package ru.cft.focus.miner.model;

import lombok.Getter;

@Getter
public class CellEvent {
    private final CellState state;
    private final int x;
    private final int y;
    private final ActionType buttonType;

    public CellEvent(int x, int y, CellState state, ActionType buttonType) {
        this.x = x;
        this.y = y;
        this.state = state;
        this.buttonType = buttonType;
    }

}

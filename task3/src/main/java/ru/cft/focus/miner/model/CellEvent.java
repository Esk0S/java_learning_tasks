package ru.cft.focus.miner.model;

import lombok.Getter;

@Getter
public class CellEvent {
    private final Cell cell;
    private final int x;
    private final int y;
    private final ActionType buttonType;

    public CellEvent(int x, int y, Cell cell, ActionType buttonType) {
        this.x = x;
        this.y = y;
        this.cell = cell;
        this.buttonType = buttonType;
    }

}

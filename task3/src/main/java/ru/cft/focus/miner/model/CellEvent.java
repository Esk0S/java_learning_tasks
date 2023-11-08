package ru.cft.focus.miner.model;

import lombok.Getter;

@Getter
public class CellEvent {
    private final CellState state;
    private final int x;
    private final int y;
    private final ButtonTypeModel buttonType;
    private int estimateBombCount;

    public CellEvent(int x, int y, CellState state, ButtonTypeModel buttonType) {
        this.x = x;
        this.y = y;
        this.state = state;
        this.buttonType = buttonType;
    }

    public CellEvent(int x, int y, CellState state, ButtonTypeModel buttonType, int estimateBombCount) {
        this(x, y, state, buttonType);
        this.estimateBombCount = estimateBombCount;
    }

}

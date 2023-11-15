package ru.cft.focus.miner.model;

import lombok.Getter;

public class Cell {
    private boolean bomb;
    @Getter
    private boolean marked;

    private CellState cellState = CellState.EMPTY;
    @Getter
    private boolean opened;

    public boolean hasBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public void setOpened() {
        opened = true;
    }

    public CellState getCellState() {
        return cellState;
    }
}

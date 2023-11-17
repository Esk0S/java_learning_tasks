package ru.cft.focus.miner.model;

import lombok.Getter;

public class Cell {
    private boolean bomb;
    @Getter
    private boolean marked;
    @Getter
    private boolean opened;
    private int bombsAround;

    public boolean hasBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public void setOpened() {
        opened = true;
    }

    public int getBombsAround() {
        return bombsAround;
    }

    public void setBombsAround(int bombsAround) {
        this.bombsAround = bombsAround;
    }

    public boolean isEmpty() {
        return !hasBomb() && getBombsAround() == 0 && !isMarked();
    }
}

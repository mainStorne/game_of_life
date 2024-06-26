package com.example.androidgame;

public class Cell {
    public int x, y;
    public boolean alive;

    public Cell(int x, int y, boolean alive) {
        this.x = x;
        this.y = y;
        this.alive = alive;
    }

    public void die() {
        this.alive = false;
    }

    public void reborn() {
        this.alive = true;
    }

    public void invert() {
        alive = !alive;
    }
}

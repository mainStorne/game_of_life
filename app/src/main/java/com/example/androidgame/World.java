package com.example.androidgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    public int width, height;

    Random random = new Random();

    public Cell[][] board;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        board = new Cell[width][height];
        init();
    }

    public void init() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board[i][j] = new Cell(i, j, random.nextBoolean());
            }
        }
    }

    public void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board[i][j] = new Cell(i, j, false);
            }
        }
    }



    public Cell get(int i, int j) {
        return board[i][j];
    }

    public int numberNeighbors(int i, int j) {
        int result = 0;
        for (int w = i - 1; w <= i + 1; w++) {
            for (int h = j - 1; h <= j + 1; h++) {
                if (
                        (w != i || h != j)
                                && w >= 0 && h >= 0
                                && w < width && h < height
                ) {
                    Cell cell = board[w][h];
                    if (cell.alive) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

    public void nextGeneration() {
        List<Cell> liveCells = new ArrayList<>();
        List<Cell> deadCells = new ArrayList<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = board[i][j];

                int neighbors = numberNeighbors(i, j);

                if ((!cell.alive && neighbors == 3)
                        ||
                        (cell.alive && (neighbors == 3 || neighbors == 2))) {
                    liveCells.add(cell);
                } else if (cell.alive && (neighbors < 2 || neighbors > 3)) {
                    deadCells.add(cell);
                }
            }

        }

        for (Cell cell : liveCells) {
            cell.reborn();
        }

        for (Cell cell : deadCells) {
            cell.die();
        }

    }

}

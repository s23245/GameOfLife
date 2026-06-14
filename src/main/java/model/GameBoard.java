package model;

import java.util.Objects;
import java.util.Random;

public class GameBoard {
    private final int rows;
    private final int columns;
    private boolean[][] cells;

    public GameBoard(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Board dimensions must be positive.");
        }
        this.rows = rows;
        this.columns = columns;
        this.cells = new boolean[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean isAlive(int row, int column) {
        validatePosition(row, column);
        return cells[row][column];
    }

    public void setAlive(int row, int column, boolean alive) {
        validatePosition(row, column);
        cells[row][column] = alive;
    }

    public void toggle(int row, int column) {
        validatePosition(row, column);
        cells[row][column] = !cells[row][column];
    }

    public void clear() {
        cells = new boolean[rows][columns];
    }

    public void randomize(Random random, double aliveProbability) {
        Objects.requireNonNull(random, "random must not be null");
        if (aliveProbability < 0.0 || aliveProbability > 1.0) {
            throw new IllegalArgumentException("Alive probability must be between 0 and 1.");
        }

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                cells[row][column] = random.nextDouble() < aliveProbability;
            }
        }
    }

    public int countAliveCells() {
        int count = 0;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (cells[row][column]) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean[][] copyCells() {
        boolean[][] copy = new boolean[rows][columns];
        for (int row = 0; row < rows; row++) {
            System.arraycopy(cells[row], 0, copy[row], 0, columns);
        }
        return copy;
    }

    public void replaceWith(boolean[][] nextCells) {
        Objects.requireNonNull(nextCells, "nextCells must not be null");
        if (nextCells.length != rows) {
            throw new IllegalArgumentException("Replacement state has the wrong row count.");
        }

        boolean[][] copy = new boolean[rows][columns];
        for (int row = 0; row < rows; row++) {
            if (nextCells[row] == null || nextCells[row].length != columns) {
                throw new IllegalArgumentException("Replacement state must be rectangular and match the board size.");
            }
            System.arraycopy(nextCells[row], 0, copy[row], 0, columns);
        }
        cells = copy;
    }

    private void validatePosition(int row, int column) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException("Cell position is outside the board: " + row + ", " + column + ".");
        }
    }
}

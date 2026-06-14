package model;

import java.util.Objects;

public final class CellPattern {
    private final String name;
    private final boolean[][] cells;

    public CellPattern(String name, boolean[][] cells) {
        this.name = validateName(name);
        this.cells = copyAndValidate(cells);
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return cells.length;
    }

    public int getColumns() {
        return cells[0].length;
    }

    public boolean isAlive(int row, int column) {
        return cells[row][column];
    }

    public void placeCentered(GameBoard board) {
        Objects.requireNonNull(board, "board must not be null");
        int topRow = (board.getRows() - getRows()) / 2;
        int leftColumn = (board.getColumns() - getColumns()) / 2;
        placeAt(board, Math.max(0, topRow), Math.max(0, leftColumn));
    }

    public void placeAt(GameBoard board, int topRow, int leftColumn) {
        Objects.requireNonNull(board, "board must not be null");
        for (int row = 0; row < getRows(); row++) {
            for (int column = 0; column < getColumns(); column++) {
                int boardRow = topRow + row;
                int boardColumn = leftColumn + column;
                if (isInside(board, boardRow, boardColumn)) {
                    board.setAlive(boardRow, boardColumn, cells[row][column]);
                }
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }

    private static boolean isInside(GameBoard board, int row, int column) {
        return row >= 0 && row < board.getRows() && column >= 0 && column < board.getColumns();
    }

    private static String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Pattern name must not be blank.");
        }
        return name.trim();
    }

    private static boolean[][] copyAndValidate(boolean[][] source) {
        Objects.requireNonNull(source, "cells must not be null");
        if (source.length == 0 || source[0] == null || source[0].length == 0) {
            throw new IllegalArgumentException("Pattern must contain at least one cell.");
        }

        int columns = source[0].length;
        boolean[][] copy = new boolean[source.length][columns];
        for (int row = 0; row < source.length; row++) {
            if (source[row] == null || source[row].length != columns) {
                throw new IllegalArgumentException("Pattern cells must be rectangular.");
            }
            System.arraycopy(source[row], 0, copy[row], 0, columns);
        }
        return copy;
    }
}

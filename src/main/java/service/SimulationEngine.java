package service;

import model.BoundaryMode;
import model.CellRule;
import model.GameBoard;

import java.util.Objects;

public class SimulationEngine {
    private CellRule rule;
    private BoundaryMode boundaryMode;

    public SimulationEngine(CellRule rule, BoundaryMode boundaryMode) {
        this.rule = Objects.requireNonNull(rule, "rule must not be null");
        this.boundaryMode = Objects.requireNonNull(boundaryMode, "boundaryMode must not be null");
    }

    public CellRule getRule() {
        return rule;
    }

    public void setRule(CellRule rule) {
        this.rule = Objects.requireNonNull(rule, "rule must not be null");
    }

    public BoundaryMode getBoundaryMode() {
        return boundaryMode;
    }

    public void setBoundaryMode(BoundaryMode boundaryMode) {
        this.boundaryMode = Objects.requireNonNull(boundaryMode, "boundaryMode must not be null");
    }

    public void nextGeneration(GameBoard board) {
        Objects.requireNonNull(board, "board must not be null");
        boolean[][] nextCells = new boolean[board.getRows()][board.getColumns()];

        for (int row = 0; row < board.getRows(); row++) {
            for (int column = 0; column < board.getColumns(); column++) {
                int aliveNeighbors = countAliveNeighbors(board, row, column);
                if (board.isAlive(row, column)) {
                    nextCells[row][column] = rule.isSurvival(aliveNeighbors);
                } else {
                    nextCells[row][column] = rule.isBirth(aliveNeighbors);
                }
            }
        }

        board.replaceWith(nextCells);
    }

    public int countAliveNeighbors(GameBoard board, int row, int column) {
        Objects.requireNonNull(board, "board must not be null");
        int aliveNeighbors = 0;

        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                if (rowOffset == 0 && columnOffset == 0) {
                    continue;
                }

                int neighborRow = row + rowOffset;
                int neighborColumn = column + columnOffset;

                if (boundaryMode == BoundaryMode.TOROIDAL) {
                    neighborRow = Math.floorMod(neighborRow, board.getRows());
                    neighborColumn = Math.floorMod(neighborColumn, board.getColumns());
                } else if (isOutside(board, neighborRow, neighborColumn)) {
                    continue;
                }

                if (neighborRow == row && neighborColumn == column) {
                    continue;
                }

                if (board.isAlive(neighborRow, neighborColumn)) {
                    aliveNeighbors++;
                }
            }
        }

        return aliveNeighbors;
    }

    private boolean isOutside(GameBoard board, int row, int column) {
        return row < 0 || row >= board.getRows() || column < 0 || column >= board.getColumns();
    }
}

package service;

import model.BoundaryMode;
import model.CellRule;
import model.GameBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulationEngineTest {
    @Test
    void centerCellsCountNeighborsCorrectly() {
        GameBoard board = new GameBoard(3, 3);
        board.setAlive(0, 1, true);
        board.setAlive(1, 0, true);
        board.setAlive(2, 1, true);
        SimulationEngine engine = new SimulationEngine(CellRule.conway(), BoundaryMode.FIXED_DEAD);

        assertEquals(3, engine.countAliveNeighbors(board, 1, 1));
    }

    @Test
    void cornerCellsCountWrappedNeighborsInToroidalMode() {
        GameBoard board = new GameBoard(3, 3);
        board.setAlive(2, 2, true);
        board.setAlive(2, 0, true);
        board.setAlive(0, 2, true);
        SimulationEngine engine = new SimulationEngine(CellRule.conway(), BoundaryMode.TOROIDAL);

        assertEquals(3, engine.countAliveNeighbors(board, 0, 0));
    }

    @Test
    void cornerCellsIgnoreOutsideNeighborsInFixedDeadMode() {
        GameBoard board = new GameBoard(3, 3);
        board.setAlive(2, 2, true);
        board.setAlive(2, 0, true);
        board.setAlive(0, 2, true);
        board.setAlive(0, 1, true);
        SimulationEngine engine = new SimulationEngine(CellRule.conway(), BoundaryMode.FIXED_DEAD);

        assertEquals(1, engine.countAliveNeighbors(board, 0, 0));
    }

    @Test
    void currentCellIsNotCountedAsItsOwnNeighbor() {
        GameBoard board = new GameBoard(1, 1);
        board.setAlive(0, 0, true);
        SimulationEngine engine = new SimulationEngine(CellRule.conway(), BoundaryMode.TOROIDAL);

        assertEquals(0, engine.countAliveNeighbors(board, 0, 0));
    }

    @Test
    void deadCellIsBornWithThreeNeighbors() {
        GameBoard board = new GameBoard(3, 3);
        board.setAlive(0, 1, true);
        board.setAlive(1, 0, true);
        board.setAlive(1, 2, true);
        SimulationEngine engine = new SimulationEngine(CellRule.conway(), BoundaryMode.FIXED_DEAD);

        engine.nextGeneration(board);

        assertTrue(board.isAlive(1, 1));
    }

    @Test
    void livingCellSurvivesWithTwoNeighbors() {
        GameBoard board = new GameBoard(3, 3);
        board.setAlive(1, 1, true);
        board.setAlive(0, 1, true);
        board.setAlive(1, 0, true);
        SimulationEngine engine = new SimulationEngine(CellRule.conway(), BoundaryMode.FIXED_DEAD);

        engine.nextGeneration(board);

        assertTrue(board.isAlive(1, 1));
    }

    @Test
    void livingCellDiesByUnderpopulation() {
        GameBoard board = new GameBoard(3, 3);
        board.setAlive(1, 1, true);
        board.setAlive(0, 1, true);
        SimulationEngine engine = new SimulationEngine(CellRule.conway(), BoundaryMode.FIXED_DEAD);

        engine.nextGeneration(board);

        assertFalse(board.isAlive(1, 1));
    }

    @Test
    void livingCellDiesByOverpopulation() {
        GameBoard board = new GameBoard(3, 3);
        board.setAlive(1, 1, true);
        board.setAlive(0, 0, true);
        board.setAlive(0, 1, true);
        board.setAlive(0, 2, true);
        board.setAlive(1, 0, true);
        SimulationEngine engine = new SimulationEngine(CellRule.conway(), BoundaryMode.FIXED_DEAD);

        engine.nextGeneration(board);

        assertFalse(board.isAlive(1, 1));
    }

    @Test
    void toroidalBoundariesCanCreateBirthAcrossEdges() {
        GameBoard board = new GameBoard(3, 3);
        board.setAlive(2, 2, true);
        board.setAlive(2, 0, true);
        board.setAlive(0, 2, true);
        SimulationEngine engine = new SimulationEngine(CellRule.conway(), BoundaryMode.TOROIDAL);

        engine.nextGeneration(board);

        assertTrue(board.isAlive(0, 0));
    }

    @Test
    void fixedDeadBoundariesDoNotCreateBirthAcrossEdges() {
        GameBoard board = new GameBoard(3, 3);
        board.setAlive(2, 2, true);
        board.setAlive(2, 0, true);
        board.setAlive(0, 2, true);
        SimulationEngine engine = new SimulationEngine(CellRule.conway(), BoundaryMode.FIXED_DEAD);

        engine.nextGeneration(board);

        assertFalse(board.isAlive(0, 0));
    }

    @Test
    void blinkerReturnsToOriginalStateAfterTwoGenerations() {
        GameBoard board = new GameBoard(5, 5);
        board.setAlive(2, 1, true);
        board.setAlive(2, 2, true);
        board.setAlive(2, 3, true);
        boolean[][] original = board.copyCells();
        SimulationEngine engine = new SimulationEngine(CellRule.conway(), BoundaryMode.FIXED_DEAD);

        engine.nextGeneration(board);
        assertTrue(board.isAlive(1, 2));
        assertTrue(board.isAlive(2, 2));
        assertTrue(board.isAlive(3, 2));

        engine.nextGeneration(board);

        assertArrayEquals(original, board.copyCells());
    }
}

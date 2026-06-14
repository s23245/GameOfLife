package model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameBoardTest {
    @Test
    void clearRemovesAllLivingCells() {
        GameBoard board = new GameBoard(3, 3);
        board.setAlive(0, 0, true);
        board.setAlive(1, 1, true);

        board.clear();

        assertEquals(0, board.countAliveCells());
    }

    @Test
    void randomizeCanFillAndEmptyBoardDeterministically() {
        GameBoard board = new GameBoard(4, 4);

        board.randomize(new Random(1), 1.0);
        assertEquals(16, board.countAliveCells());

        board.randomize(new Random(1), 0.0);
        assertEquals(0, board.countAliveCells());
    }

    @Test
    void replaceWithCopiesInputState() {
        GameBoard board = new GameBoard(2, 2);
        boolean[][] nextState = {
                {true, false},
                {false, true}
        };

        board.replaceWith(nextState);
        nextState[0][0] = false;

        assertTrue(board.isAlive(0, 0));
        assertTrue(board.isAlive(1, 1));
    }

    @Test
    void copyCellsReturnsIndependentArray() {
        GameBoard board = new GameBoard(2, 2);
        board.setAlive(0, 0, true);

        boolean[][] copy = board.copyCells();
        copy[0][0] = false;

        assertNotSame(copy, board.copyCells());
        assertTrue(board.isAlive(0, 0));
        assertFalse(copy[0][0]);
    }
}

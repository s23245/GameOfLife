package ui;

import model.GameBoard;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class GridPanel extends JPanel {
    private static final int PREFERRED_SIZE = 760;
    private static final Color BACKGROUND_COLOR = new Color(246, 248, 250);
    private static final Color ALIVE_COLOR = new Color(234, 194, 222);
    private static final Color DEAD_COLOR = new Color(238, 242, 244);
    private static final Color GRID_COLOR = new Color(190, 199, 203);

    private final GameBoard board;
    private final Runnable boardChangedCallback;

    public GridPanel(GameBoard board, Runnable boardChangedCallback) {
        this.board = Objects.requireNonNull(board, "board must not be null");
        this.boardChangedCallback = Objects.requireNonNull(boardChangedCallback, "boardChangedCallback must not be null");

        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(PREFERRED_SIZE, PREFERRED_SIZE));
        setMinimumSize(new Dimension(360, 360));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                toggleCell(event);
            }
        });
    }

    public void repaintCell(int row, int column) {
        GridMetrics metrics = calculateGridMetrics();
        if (!metrics.hasDrawableArea()) {
            repaint();
            return;
        }

        int x = metrics.xForColumn(column);
        int y = metrics.yForRow(row);
        int size = (int) Math.ceil(metrics.cellSize()) + 2;
        repaint(x - 1, y - 1, size, size);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        GridMetrics metrics = calculateGridMetrics();
        graphics2D.setColor(BACKGROUND_COLOR);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());

        if (!metrics.hasDrawableArea()) {
            graphics2D.dispose();
            return;
        }

        for (int row = 0; row < board.getRows(); row++) {
            for (int column = 0; column < board.getColumns(); column++) {
                int x = metrics.xForColumn(column);
                int y = metrics.yForRow(row);
                int nextX = metrics.xForColumn(column + 1);
                int nextY = metrics.yForRow(row + 1);

                graphics2D.setColor(board.isAlive(row, column) ? ALIVE_COLOR : DEAD_COLOR);
                graphics2D.fillRect(x, y, Math.max(1, nextX - x), Math.max(1, nextY - y));

                graphics2D.setColor(GRID_COLOR);
                graphics2D.drawRect(x, y, Math.max(1, nextX - x), Math.max(1, nextY - y));
            }
        }

        graphics2D.dispose();
    }

    private void toggleCell(MouseEvent event) {
        GridMetrics metrics = calculateGridMetrics();
        if (!metrics.hasDrawableArea()) {
            return;
        }

        int row = metrics.rowAt(event.getY());
        int column = metrics.columnAt(event.getX());
        if (row < 0 || row >= board.getRows() || column < 0 || column >= board.getColumns()) {
            return;
        }

        board.toggle(row, column);
        boardChangedCallback.run();
        repaintCell(row, column);
    }

    private GridMetrics calculateGridMetrics() {
        double cellSize = Math.min(
                getWidth() / (double) board.getColumns(),
                getHeight() / (double) board.getRows()
        );

        if (cellSize <= 0.0 || Double.isNaN(cellSize)) {
            return new GridMetrics(0, 0, 0);
        }

        int gridWidth = (int) Math.floor(cellSize * board.getColumns());
        int gridHeight = (int) Math.floor(cellSize * board.getRows());
        int originX = (getWidth() - gridWidth) / 2;
        int originY = (getHeight() - gridHeight) / 2;

        return new GridMetrics(originX, originY, cellSize);
    }

    private record GridMetrics(int originX, int originY, double cellSize) {
        boolean hasDrawableArea() {
            return cellSize > 0.0;
        }

        int xForColumn(int column) {
            return originX + (int) Math.round(column * cellSize);
        }

        int yForRow(int row) {
            return originY + (int) Math.round(row * cellSize);
        }

        int columnAt(int x) {
            return (int) Math.floor((x - originX) / cellSize);
        }

        int rowAt(int y) {
            return (int) Math.floor((y - originY) / cellSize);
        }
    }
}

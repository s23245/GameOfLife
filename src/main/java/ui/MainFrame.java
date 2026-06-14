package ui;

import model.BoundaryMode;
import model.CellPattern;
import model.CellRule;
import model.GameBoard;
import model.PatternLibrary;
import service.SimulationEngine;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Random;

public class MainFrame extends JFrame implements ControlPanel.Listener {
    private static final int DEFAULT_ROWS = 40;
    private static final int DEFAULT_COLUMNS = 40;
    private static final double RANDOM_ALIVE_PROBABILITY = 0.28;

    private final GameBoard board;
    private final SimulationEngine simulationEngine;
    private final GridPanel gridPanel;
    private final ControlPanel controlPanel;
    private final Timer timer;

    private int generation;

    public MainFrame() {
        super("Configurable Cellular Automata");

        this.board = new GameBoard(DEFAULT_ROWS, DEFAULT_COLUMNS);
        this.simulationEngine = new SimulationEngine(CellRule.conway(), BoundaryMode.TOROIDAL);
        this.gridPanel = new GridPanel(board, this::updateStatistics);
        this.controlPanel = new ControlPanel(
                simulationEngine.getRule(),
                simulationEngine.getBoundaryMode(),
                PatternLibrary.getPatterns(),
                this
        );
        this.timer = new Timer(controlPanel.getSelectedDelayMillis(), event -> advanceOneGeneration());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 640));
        setLayout(new BorderLayout(12, 12));

        JPanel contentPanel = new JPanel(new BorderLayout(12, 12));
        contentPanel.add(gridPanel, BorderLayout.CENTER);
        contentPanel.add(controlPanel, BorderLayout.EAST);
        setContentPane(contentPanel);

        updateStatistics();
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void onStartRequested() {
        if (!timer.isRunning()) {
            timer.start();
            controlPanel.setRunning(true);
        }
    }

    @Override
    public void onPauseRequested() {
        pauseSimulation();
    }

    @Override
    public void onNextGenerationRequested() {
        pauseSimulation();
        advanceOneGeneration();
    }

    @Override
    public void onClearRequested() {
        pauseSimulation();
        board.clear();
        generation = 0;
        gridPanel.repaint();
        updateStatistics();
    }

    @Override
    public void onRandomizeRequested() {
        pauseSimulation();
        board.randomize(new Random(), RANDOM_ALIVE_PROBABILITY);
        generation = 0;
        gridPanel.repaint();
        updateStatistics();
    }

    @Override
    public void onRuleSubmitted(String ruleText) {
        try {
            CellRule rule = CellRule.parse(ruleText);
            simulationEngine.setRule(rule);
            controlPanel.setRuleText(rule.toString());
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Invalid rule",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public void onBoundaryModeChanged(BoundaryMode boundaryMode) {
        simulationEngine.setBoundaryMode(boundaryMode);
    }

    @Override
    public void onPatternPlacementRequested(CellPattern pattern) {
        pauseSimulation();
        pattern.placeCentered(board);
        generation = 0;
        gridPanel.repaint();
        updateStatistics();
    }

    @Override
    public void onSpeedChanged(int delayMillis) {
        timer.setDelay(delayMillis);
        timer.setInitialDelay(delayMillis);
    }

    private void advanceOneGeneration() {
        simulationEngine.nextGeneration(board);
        generation++;
        gridPanel.repaint();
        updateStatistics();
    }

    private void pauseSimulation() {
        if (timer.isRunning()) {
            timer.stop();
        }
        controlPanel.setRunning(false);
    }

    private void updateStatistics() {
        controlPanel.updateStatistics(generation, board.countAliveCells());
    }
}

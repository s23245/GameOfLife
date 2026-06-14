package ui;

import model.BoundaryMode;
import model.CellPattern;
import model.CellRule;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.Objects;

public class ControlPanel extends JPanel {
    private static final int MIN_SPEED = 1;
    private static final int MAX_SPEED = 10;
    private static final int DEFAULT_SPEED = 5;

    private final Listener listener;
    private final JButton startButton = new JButton("Start");
    private final JButton pauseButton = new JButton("Pause");
    private final JTextField ruleField;
    private final JComboBox<RulePreset> presetComboBox;
    private final JComboBox<BoundaryMode> boundaryModeComboBox;
    private final JComboBox<CellPattern> patternComboBox;
    private final JSlider speedSlider = new JSlider(SwingConstants.HORIZONTAL, MIN_SPEED, MAX_SPEED, DEFAULT_SPEED);
    private final JLabel generationLabel = new JLabel("0");
    private final JLabel livingCellsLabel = new JLabel("0");

    public ControlPanel(CellRule currentRule,
                        BoundaryMode currentBoundaryMode,
                        List<CellPattern> patterns,
                        Listener listener) {
        this.listener = Objects.requireNonNull(listener, "listener must not be null");
        Objects.requireNonNull(currentRule, "currentRule must not be null");
        Objects.requireNonNull(currentBoundaryMode, "currentBoundaryMode must not be null");
        Objects.requireNonNull(patterns, "patterns must not be null");

        this.ruleField = new JTextField(currentRule.toString(), 10);
        this.presetComboBox = new JComboBox<>(RulePreset.defaultPresets());
        this.boundaryModeComboBox = new JComboBox<>(BoundaryMode.values());
        this.patternComboBox = new JComboBox<>(patterns.toArray(CellPattern[]::new));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(14, 14, 14, 14));
        setPreferredSize(new Dimension(330, 720));

        boundaryModeComboBox.setSelectedItem(currentBoundaryMode);

        add(createSimulationSection());
        add(Box.createVerticalStrut(12));
        add(createStatsSection());
        add(Box.createVerticalStrut(12));
        add(createRuleSection());
        add(Box.createVerticalStrut(12));
        add(createBoundarySection());
        add(Box.createVerticalStrut(12));
        add(createPatternSection());
        add(Box.createVerticalGlue());
    }

    public int getSelectedDelayMillis() {
        return speedToDelayMillis(speedSlider.getValue());
    }

    public void setRunning(boolean running) {
        startButton.setEnabled(!running);
        pauseButton.setEnabled(running);
    }

    public void updateStatistics(int generation, int livingCells) {
        generationLabel.setText(String.valueOf(generation));
        livingCellsLabel.setText(String.valueOf(livingCells));
    }

    public void setRuleText(String ruleText) {
        ruleField.setText(ruleText);
    }

    private JPanel createSimulationSection() {
        JPanel panel = createSectionPanel("Simulation");
        panel.setLayout(new GridLayout(0, 2, 8, 8));

        JButton nextButton = new JButton("Next generation");
        JButton clearButton = new JButton("Clear");
        JButton randomizeButton = new JButton("Randomize");

        startButton.addActionListener(event -> listener.onStartRequested());
        pauseButton.addActionListener(event -> listener.onPauseRequested());
        nextButton.addActionListener(event -> listener.onNextGenerationRequested());
        clearButton.addActionListener(event -> listener.onClearRequested());
        randomizeButton.addActionListener(event -> listener.onRandomizeRequested());

        panel.add(startButton);
        panel.add(pauseButton);
        panel.add(nextButton);
        panel.add(clearButton);
        panel.add(randomizeButton);
        panel.add(createSpeedPanel());

        setRunning(false);
        return panel;
    }

    private JPanel createSpeedPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 2));
        JLabel label = new JLabel("Speed", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 12f));

        speedSlider.setMajorTickSpacing(3);
        speedSlider.setPaintTicks(true);
        speedSlider.setToolTipText("Simulation speed");
        speedSlider.addChangeListener(event -> listener.onSpeedChanged(getSelectedDelayMillis()));

        panel.add(label, BorderLayout.NORTH);
        panel.add(speedSlider, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatsSection() {
        JPanel panel = createSectionPanel("Counters");
        panel.setLayout(new GridLayout(2, 2, 8, 8));

        panel.add(new JLabel("Generation"));
        panel.add(generationLabel);
        panel.add(new JLabel("Living cells"));
        panel.add(livingCellsLabel);
        return panel;
    }

    private JPanel createRuleSection() {
        JPanel panel = createSectionPanel("Rules");
        panel.setLayout(new BorderLayout(8, 8));

        JButton applyRuleButton = new JButton("Apply");
        applyRuleButton.addActionListener(event -> listener.onRuleSubmitted(ruleField.getText()));
        ruleField.addActionListener(event -> listener.onRuleSubmitted(ruleField.getText()));

        JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
        inputPanel.add(ruleField, BorderLayout.CENTER);
        inputPanel.add(applyRuleButton, BorderLayout.EAST);

        presetComboBox.addActionListener(event -> {
            RulePreset selectedPreset = (RulePreset) presetComboBox.getSelectedItem();
            if (selectedPreset != null) {
                ruleField.setText(selectedPreset.ruleText());
                listener.onRuleSubmitted(selectedPreset.ruleText());
            }
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(labeledPanel("Presets", presetComboBox), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBoundarySection() {
        JPanel panel = createSectionPanel("Boundary mode");
        panel.setLayout(new BorderLayout());
        boundaryModeComboBox.addActionListener(event -> {
            BoundaryMode selectedMode = (BoundaryMode) boundaryModeComboBox.getSelectedItem();
            if (selectedMode != null) {
                listener.onBoundaryModeChanged(selectedMode);
            }
        });
        panel.add(boundaryModeComboBox, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPatternSection() {
        JPanel panel = createSectionPanel("Patterns");
        panel.setLayout(new BorderLayout(8, 8));

        JButton placePatternButton = new JButton("Place near center");
        placePatternButton.addActionListener(event -> {
            CellPattern selectedPattern = (CellPattern) patternComboBox.getSelectedItem();
            if (selectedPattern != null) {
                listener.onPatternPlacementRequested(selectedPattern);
            }
        });

        panel.add(patternComboBox, BorderLayout.CENTER);
        panel.add(placePatternButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                new EmptyBorder(8, 8, 8, 8)
        ));
        return panel;
    }

    private JPanel labeledPanel(String labelText, JComboBox<?> comboBox) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.add(new JLabel(labelText + ": "));
        panel.add(comboBox);
        return panel;
    }

    private int speedToDelayMillis(int speed) {
        return 1100 - (speed * 100);
    }

    public interface Listener {
        void onStartRequested();

        void onPauseRequested();

        void onNextGenerationRequested();

        void onClearRequested();

        void onRandomizeRequested();

        void onRuleSubmitted(String ruleText);

        void onBoundaryModeChanged(BoundaryMode boundaryMode);

        void onPatternPlacementRequested(CellPattern pattern);

        void onSpeedChanged(int delayMillis);
    }

    private record RulePreset(String name, String ruleText) {
        static RulePreset[] defaultPresets() {
            return new RulePreset[]{
                    new RulePreset("Conway's Life", "B3/S23"),
                    new RulePreset("HighLife", "B36/S23"),
                    new RulePreset("Seeds", "B2/S"),
                    new RulePreset("Day & Night", "B3678/S34678")
            };
        }

        @Override
        public String toString() {
            return name + " (" + ruleText + ")";
        }
    }
}

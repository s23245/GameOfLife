import ui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception exception) {
                System.err.println("Could not apply Nimbus look and feel: " + exception.getMessage());
            }

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}

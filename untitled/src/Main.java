import Controller.MainController;
import View.MainFrame;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            new MainController(mainFrame);
        });
    }
}

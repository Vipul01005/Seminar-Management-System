package main;
import gui.LoginFrame;
import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
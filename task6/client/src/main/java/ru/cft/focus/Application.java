package ru.cft.focus;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClientInterface::new);
    }
}

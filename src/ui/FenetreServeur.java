package ui;

import server.Server;

import javax.swing.*;

public class FenetreServeur extends JFrame {
    private JTextArea chatArea;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JTextField portInput;
    private JButton demarrerButton;
    private Server server;

    public FenetreServeur() {
        super("Fenetre Serveur");
        add(mainPanel);

        chatArea.setEnabled(false);

        demarrerButton.addActionListener(e -> demarrerServeur());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 250);
        this.setVisible(true);
    }

    public void demarrerServeur() {
        int port = Integer.parseInt(portInput.getText());
        server = new Server(port, this);
        server.start();

        portInput.setEnabled(false);
        demarrerButton.setEnabled(false);
    }

    public void displayNewMessage(String message) {
        // Ajoute le message
        chatArea.append(message + "\n");

        // Va en bas de la fenêtre
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public static void main(String[] args) {
        new FenetreServeur();
    }
}

/*
 * FenetreServeur.java, 06/12/2022
 * INU Champollion, 2022-2023
 * pas de copyright, aucun droits
 */

package ui;

import server.Server;

import javax.swing.*;
import java.awt.*;

/**
 * Interface du serveur
 *
 * @author Gaël Burguès
 * @author Laurian Dufrechou
 * @author Lucàs Vabre
 */
public class FenetreServeur extends JFrame {
    private JTextArea chatArea;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JTextField portInput;
    private JButton demarrerButton;

    /**
     * Le serveur lié à cette fenêtre
     */
    private Server server;

    /**
     * Crée une nouvelle fenêtre de serveur
     */
    public FenetreServeur() {
        super("Fenetre Serveur");
        add(mainPanel);

        // Configure le tchat
        chatArea.setEnabled(false);
        chatArea.setLineWrap(true);
        chatArea.setDisabledTextColor(Color.BLACK);

        // Actions
        demarrerButton.addActionListener(e -> demarrerServeur());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 250);
        this.setVisible(true);
    }

    /**
     * Crée une nouvelle instance de serveur sur le port saisi par l'utilisateur
     */
    public void demarrerServeur() {
        int port = Integer.parseInt(portInput.getText());

        // Crée le serveur
        server = new Server(port, this);
        server.start();

        // Désactive le formulaire de connexion (la saisie du port et le bouton)
        portInput.setEnabled(false);
        demarrerButton.setEnabled(false);
    }

    /**
     * Affiche un nouveau message dans le tchat
     *
     * @param message Message à afficher
     */
    public void displayNewMessage(String message) {
        // Ajoute le message
        chatArea.append(String.format("%s\n", message));

        // Va en bas de la fenêtre
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    /**
     * Lance une nouvelle fenêtre de serveur
     *
     * @param args non utilisé
     */
    public static void main(String[] args) {
        new FenetreServeur();
    }
}

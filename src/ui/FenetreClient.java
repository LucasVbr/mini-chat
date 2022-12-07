/*
 * FenetreClient.java, 06/12/2022
 * INU Champollion, 2022-2023
 * pas de copyright, aucun droits
 */

package ui;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Objects;

/**
 * Interface du client
 *
 * @author Gaël Burguès
 * @author Laurian Dufrechou
 * @author Lucàs Vabre
 */
public class FenetreClient extends JFrame {
    private JTextField addressInput, portInput, pseudoInput, messageInput;
    private JButton connexionButton, envoyerButton, deconnexionButton;
    private JPanel mainPanel;
    private JTextArea chatArea;
    private JScrollPane scrollPane;

    /**
     * Le client lié à cette fenêtre
     */
    private Client client;

    /**
     * Crée une nouvelle fenêtre client
     */
    public FenetreClient() {
        super("Fenêtre client");
        add(mainPanel);

        // Configure le tchat
        chatArea.setEnabled(false);
        chatArea.setLineWrap(true);
        chatArea.setDisabledTextColor(Color.BLACK);

        // Au lancement, on ne peut pas envoyer de message sans être connecté au serveur
        messageInput.setEnabled(false);
        envoyerButton.setEnabled(false);
        deconnexionButton.setEnabled(false);

        // Action des boutons
        messageInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) envoyerMessage();
            }
        });
        connexionButton.addActionListener(e -> connexion());
        envoyerButton.addActionListener(e -> envoyerMessage());
        deconnexionButton.addActionListener(e -> deconnexion());

        // Quand on clique sur fermer la fenêtre, on ferme la connexion.
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                deconnexion();
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 250);
        this.setVisible(true);
    }

    /**
     * Récupère la saisie de l'utilisateur et l'envoie au serveur
     */
    private void envoyerMessage() {
        {
            String message = this.messageInput.getText();

            try {
                if (Objects.equals(message, "bye")) deconnexion();
                else this.client.sendMessage(message);

                this.messageInput.setText("");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Crée un client et le connecte au serveur à partir de l'IP et port du serveur avec le pseudo du client.
     */
    public void connexion() {
        String address = addressInput.getText();
        int port = Integer.parseInt(portInput.getText());
        String pseudo = pseudoInput.getText();

        if (Objects.equals(pseudo.strip(), "")) {
            new FenetreErreur("Pseudo vide !", this);
            this.pseudoInput.setText("");
            return;
        }

        this.client = new Client(address, port, pseudo, this);
        this.client.start();

        // Désactive le formulaire de connexion
        addressInput.setEnabled(false);
        portInput.setEnabled(false);
        pseudoInput.setEnabled(false);
        connexionButton.setEnabled(false);

        // Active l'interface de tchat
        messageInput.setEnabled(true);
        envoyerButton.setEnabled(true);
        deconnexionButton.setEnabled(true);
    }

    /**
     * Déconnecte le client du serveur
     */
    public void deconnexion() {
        if (client == null) return;

        // Déconnecte le client
        this.client.addMessage("bye");
        this.client = null;

        // Vide le tchat
        this.chatArea.setText("");

        // Active le formulaire de connexion
        addressInput.setEnabled(true);
        portInput.setEnabled(true);
        pseudoInput.setEnabled(true);
        connexionButton.setEnabled(true);

        // Désactive l'interface de tchat
        messageInput.setEnabled(false);
        envoyerButton.setEnabled(false);
        deconnexionButton.setEnabled(false);
    }

    /**
     * Affiche un nouveau message dans la fenêtre de tchat
     * @param message message à afficher dans le tchat
     */
    public void displayNewMessage(String message) {
        // Ajoute le message dans l'interface de tchat
        chatArea.append(String.format("%s\n", message));

        // Va en bas de la fenêtre
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    /**
     * Lance une nouvelle fenêtre de client
     * @param args non utilisé
     */
    public static void main(String[] args) {
        new FenetreClient();
    }
}

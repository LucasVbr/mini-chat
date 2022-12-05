package ui;

import client.Client;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Objects;

public class FenetreClient extends JFrame {
    private JTextField addressInput, portInput, pseudoInput, messageInput;
    private JButton connexionButton, envoyerButton, deconnexionButton;
    private JPanel mainPanel;
    private JTextArea chatArea;
    private JScrollPane scrollPane;

    private Client client;

    public FenetreClient() {
        super("Fenêtre client");
        add(mainPanel);

        // On ne peut pas saisir dans la boîte de dialogue directement
        chatArea.setEnabled(false);

        // Au lancement, on ne peut pas envoyer de message sans être connecté au serveur
        messageInput.setEnabled(false);
        envoyerButton.setEnabled(false);

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

        // Quand on clique sur fermer la fenêtre, on ferme la connexion
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {deconnexion();}
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 250);
        this.setVisible(true);
    }

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

        addressInput.setEnabled(false);
        portInput.setEnabled(false);
        pseudoInput.setEnabled(false);

        messageInput.setEnabled(true);
        envoyerButton.setEnabled(true);
    }

    public void deconnexion() {
        if (client == null) return;

        // Déconnecte le client
        this.client.addMessage("bye");
        this.client = null;

        // Vide le chat
        this.chatArea.setText("");

        // Active les boutons pour changer de serveur
        addressInput.setEnabled(true);
        portInput.setEnabled(true);
        pseudoInput.setEnabled(true);

        // Désactive les commandes de chats
        messageInput.setEnabled(false);
        envoyerButton.setEnabled(false);
    }

    public void displayNewMessage(String message) {
        // Ajoute le message
        chatArea.append(message + "\n");

        // Va en bas de la fenêtre
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public static void main(String[] args) {
        new FenetreClient();
    }
}

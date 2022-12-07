/*
 * Client.java, 06/12/2022
 * INU Champollion, 2022-2023
 * pas de copyright, aucun droits
 */

package client;

import ui.FenetreClient;
import ui.FenetreErreur;
import utils.RSA;
import utils.ResolutionDeNom;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Utilisateur Client
 *
 * @author Gaël Burguès
 * @author Laurian Dufrechou
 * @author Lucàs Vabre
 */

public class Client extends Thread {

    /**
     * Adresse IP de connexion au serveur
     */
    private final String SERVER_IP;

    /**
     * Port de connexion du serveur
     */
    private final int SERVER_PORT;

    /**
     * Interface utilisateur
     */
    private final FenetreClient fenetre;

    /**
     * Le pseudonyme du client
     */
    private String pseudo;

    /**
     * Paire de clés de chiffrage du client
     */
    private final KeyPair clientKeys;

    /**
     * Clé publique du serveur
     */
    private PublicKey serverKey;

    /**
     * Flux d'entrée pour la version console
     */
    private final Scanner scanner;

    /**
     * Flux d'entrée du socket
     */
    private ObjectInputStream inputStream;

    /**
     * Flux de sortie du socket
     */
    private ObjectOutputStream outputStream;

    /**
     * File d'attente des messages à envoyer
     */
    private final ArrayList<String> fileAttenteMessage;

    /**
     * Crée un nouveau client qui se connecte à un serveur
     *
     * @param ip   Adresse du serveur (IP ou URI)
     * @param port Port de connexion
     */
    public Client(String ip, int port) {
        this.SERVER_IP = ResolutionDeNom.getIPAddress(ip);
        this.SERVER_PORT = port;
        this.pseudo = null;
        this.fenetre = null;

        this.scanner = new Scanner(System.in);
        this.clientKeys = RSA.genererCle();

        this.fileAttenteMessage = new ArrayList<>();
    }

    /**
     * Crée un nouveau client qui se connecte à un serveur avec un pseudo
     *
     * @param ip     Adresse du serveur (IP ou URI)
     * @param port   Port de connexion
     * @param pseudo Le pseudo du client
     */
    public Client(String ip, int port, String pseudo) {
        this.SERVER_IP = ResolutionDeNom.getIPAddress(ip);
        this.SERVER_PORT = port;
        this.pseudo = pseudo;
        this.fenetre = null;

        this.scanner = new Scanner(System.in);
        this.clientKeys = RSA.genererCle();

        this.fileAttenteMessage = new ArrayList<>();
    }

    /**
     * Crée un nouveau client lié à une interface utilisateur
     *
     * @param ip      Adresse du serveur (IP ou URI)
     * @param port    Port de connexion au serveur
     * @param pseudo  Pseudonyme du client
     * @param fenetre Interface du client
     */
    public Client(String ip, int port, String pseudo, FenetreClient fenetre) {
        this.SERVER_IP = ResolutionDeNom.getIPAddress(ip);
        this.SERVER_PORT = port;
        this.pseudo = pseudo;
        this.fenetre = fenetre;

        this.scanner = new Scanner(System.in);
        this.clientKeys = RSA.genererCle();

        this.fileAttenteMessage = new ArrayList<>();
    }

    @Override
    public void run() {
        // Demande la saisie du pseudo s'il n'est pas défini
        if (pseudo == null) {
            System.out.print("Pseudo: ");
            pseudo = scanner.nextLine();
        }

        // Connexion au socket
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            // Initialisation : envoi des clés et du pseudo
            exchangeKeys();
            sendMessage(pseudo);

            // Écoute du serveur
            ListenThread threadClient = new ListenThread(this, fenetre);
            threadClient.start();

            // Écoute de l'entrée du clavier
            System.out.println("Tapez 'bye' pour quitter\n");
            String message = "";
            do {
                // S'il y a un message dans la file d'attente, on l'envoie
                if (fileAttenteMessage.size() > 0) {
                    message = fileAttenteMessage.remove(0);
                    sendMessage(message);
                }
            } while (!Objects.equals(message, "bye"));

        } catch (ConnectException e) {
            System.err.println("Serveur non trouvé");

            if (fenetre != null) { // Affiche l'erreur sur l'interface utilisateur
                new FenetreErreur("Serveur non trouvé", fenetre);
                fenetre.deconnexion();
            }
        } catch (EOFException e) {
            System.err.println("Connexion perdue");
            if (fenetre != null) { // Affiche l'erreur sur l'interface utilisateur
                new FenetreErreur("Connexion perdue", fenetre);
                fenetre.deconnexion();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Échange les clés publiques du client et du serveur
     *
     * @throws IOException            Erreur de connexion
     * @throws ClassNotFoundException L'objet reçu n'a pas pu être cast en PublicKey
     */
    private void exchangeKeys() throws IOException, ClassNotFoundException {
        // Envoie sa clé
        outputStream.writeObject(clientKeys.getPublic());

        // Attend la clé du serveur
        serverKey = (PublicKey) inputStream.readObject();
    }

    /**
     * Envoie un message au serveur
     *
     * @param message Le message à envoyer
     * @throws IOException Erreur de connexion au socket
     */
    public void sendMessage(String message) throws IOException {
        byte[] messageCrypte = RSA.encrypter(message, serverKey);
        outputStream.writeObject(messageCrypte);
    }

    /**
     * Récupère un message du serveur
     *
     * @return Le message reçu
     * @throws IOException            Erreur de connexion au serveur
     * @throws ClassNotFoundException L'objet reçu n'a pas pu être cast en byte[]
     */
    public String getMessage() throws IOException, ClassNotFoundException {
        byte[] messageCrypte = (byte[]) inputStream.readObject();
        return RSA.decrypter(messageCrypte, clientKeys.getPrivate());
    }

    /**
     * Ajoute un message dans la file d'attente des messages à envoyer
     *
     * @param message Le message à envoyer
     */
    public void addMessage(String message) {
        this.fileAttenteMessage.add(message);
    }
}

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

public class Client extends Thread{

    //elements d'un client
    private final String SERVER_IP;
    private final int SERVER_PORT;
    private final FenetreClient fenetre;
    private String pseudo;
    private KeyPair clientKeys;
    private PublicKey serverKey;

    private final Scanner scanner;

    // Sockets de connexion
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private ArrayList<String> fileAttenteMessage;

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
     * ancien object client sans fenetre
     * @param ip adresse ip du client
     * @param port numeros de port du client
     * @param pseudo pseudo du client
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
     * Object client actuel
     * @param ip adresse ip du client
     * @param port numeros de port du client
     * @param pseudo nom du client
     * @param fenetre fenetreClient assicié au client
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
        if (pseudo == null) {
            System.out.print("Pseudo: ");
            pseudo = scanner.nextLine();
        }

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            exchangeKeys();
            sendMessage(pseudo);

            // Ecoute du serveur
            ListenThread threadClient = new ListenThread(this, fenetre);
            threadClient.start();

            // Ecoute de l'entrée du clavier
            System.out.println("Tapez 'bye' pour quitter\n");
            String message = "";
            do {
                if (fileAttenteMessage.size() > 0) {
                    message = fileAttenteMessage.remove(0);
                    sendMessage(message);
                }
            } while (!Objects.equals(message, "bye"));

        } catch (ConnectException e) {
            System.err.println("Serveur non trouvé"); // TODO throw error
            if (fenetre != null) {
                new FenetreErreur("Serveur non trouvé", fenetre);
                fenetre.deconnexion();
            }
        } catch (EOFException e) {
            System.err.println("Connexion perdue"); // TODO throw error
            if (fenetre != null) {
                new FenetreErreur("Connexion perdue", fenetre);
                fenetre.deconnexion();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * envoie la clef client au serveur
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void exchangeKeys() throws IOException, ClassNotFoundException {
        // Envoie sa clé
        outputStream.writeObject(clientKeys.getPublic());

        // Attend la clé du serveur
        serverKey = (PublicKey) inputStream.readObject();
    }

    /**
     * envoie un message dans la socket
     * @param message message a envoyer au serveur
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        byte[] messageCrypte = RSA.encrypter(message, serverKey);
        outputStream.writeObject(messageCrypte);
    }

    /**
     * Recupere un message evoye par le serveur
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public String getMessage() throws IOException, ClassNotFoundException {
        byte[] messageCrypte = (byte[]) inputStream.readObject();
        return RSA.decrypter(messageCrypte, clientKeys.getPrivate());
    }

    /**
     * Ajoute un message à la file d'attente
     * @param message messsage a ajouter a la file
     */
    public void addMessage(String message) {
        this.fileAttenteMessage.add(message);
    }
}

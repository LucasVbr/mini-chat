/*
 * ThreadServer.java, 06/12/2022
 * INU Champollion, 2022-2023
 * pas de copyright, aucun droits
 */

package server;

import ui.FenetreServeur;
import utils.RSA;

import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Thread dédié à l'écoute de message venant d'un client
 *
 * @author Gaël Burguès
 * @author Laurian Dufrechou
 * @author Lucàs Vabre
 * @see Thread
 */

public class ThreadServer extends Thread {

    /**
     * Référence des autres threads entre le serveur et d'autres clients
     */
    private final ArrayList<ThreadServer> threads;

    /**
     * Flux d'entrée du socket
     */
    private final ObjectInputStream inputStream;

    /**
     * Flux de sortie du socket
     */
    private final ObjectOutputStream outputStream;

    /**
     * Les clés RSA du serveur
     */
    private final KeyPair serverKeys;
    private PublicKey clientKey;

    /**
     * Interface graphique
     */
    private final FenetreServeur fenetre;

    /**
     * Pseudo du client
     */
    private String clientPseudo;

    /**
     * Crée un thread pour initialiser la connexion entre le serveur et un client(socket) sans interface
     *
     * @param socket  Socket du client
     * @param threads Référence vers la liste des autres threads du serveur
     * @throws IOException Si le serveur n'arrive pas à récupérer les flux d'entrée/sortie du socket client
     */
    public ThreadServer(Socket socket, ArrayList<ThreadServer> threads) throws IOException {
        this.threads = threads;
        this.fenetre = null;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());

        this.serverKeys = RSA.genererCle();
    }

    /**
     * Crée un thread pour initialiser la connexion entre le serveur et un client(socket) avec une interface
     *
     * @param socket  Socket du client
     * @param threads Référence vers la liste des autres threads du serveur
     * @param fenetre Interface graphique
     * @throws IOException Si le serveur n'arrive pas à récupérer les flux d'entrée/sortie du socket client
     */
    public ThreadServer(Socket socket, ArrayList<ThreadServer> threads, FenetreServeur fenetre) throws IOException {
        this.threads = threads;
        this.fenetre = fenetre;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());

        this.serverKeys = RSA.genererCle();
    }

    @Override
    public void run() {
        try {
            exchangeKeys();

            // Récupère le pseudo
            this.clientPseudo = getMessage();
            sendMessageToEveryone(String.format("** %s viens de rejoindre le salon **", clientPseudo), true);

            String message;
            do {
                message = getMessage();
                String reply = String.format("%s : %s", clientPseudo, message);

                sendMessageToEveryone(reply, true);
            } while (!Objects.equals(message, "bye"));
        } catch (EOFException e) {
            System.out.printf("%s c'est déconnecté\n", clientPseudo);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            threads.remove(this);
            try {
                sendMessageToEveryone(String.format("** %s viens de quitter le salon **", clientPseudo), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Envoie la cle public du serveur à la reception de celle du client
     *
     * @throws IOException            Erreur de connexion avec le socket
     * @throws ClassNotFoundException La classe {@link PublicKey} n'est pas trouvée
     */
    public void exchangeKeys() throws IOException, ClassNotFoundException {
        // Attend la clé du client
        clientKey = (PublicKey) inputStream.readObject();

        // Envoie sa clé
        outputStream.writeObject(serverKeys.getPublic());
    }

    /**
     * Attend un message du client (fonction bloquante)
     *
     * @return Le message reçu
     * @throws EOFException           Le socket est fermé
     * @throws IOException            Erreur de connexion avec le socket
     * @throws ClassNotFoundException La classe byte[] n'est pas trouvée
     */
    public String getMessage() throws EOFException, IOException, ClassNotFoundException {
        byte[] messageCrypte = (byte[]) inputStream.readObject();
        return RSA.decrypter(messageCrypte, serverKeys.getPrivate());
    }

    /**
     * Envoie un message au client
     *
     * @param message message envoyé au client
     * @param log     Si vrai affiche dans la console
     * @throws IOException Erreur de connexion avec le socket
     */
    public void sendMessage(String message, boolean log) throws IOException {
        byte[] messageCrypte = RSA.encrypter(message, clientKey);
        outputStream.writeObject(messageCrypte);

        // Log
        if (!log) return;
        System.out.println(message);
        if (fenetre != null) fenetre.displayNewMessage(message);
    }

    /**
     * Envoie un message à tous les clients connecté au serveur
     *
     * @param message Message à envoyer à tous les clients
     * @param log     si vrai affiche dans la console
     * @throws IOException Erreur de connexion avec un des sockets du serveur
     */
    public void sendMessageToEveryone(String message, boolean log) throws IOException {
        for (ThreadServer thread : threads) {
            thread.sendMessage(message, false);
        }

        // Log
        if (!log) return;
        System.out.println(message);
        if (fenetre != null) fenetre.displayNewMessage(message);
    }
}

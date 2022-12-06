/*
 * Server.java, 06/12/2022
 * INU Champollion, 2022-2023
 * pas de copyright, aucun droits
 */

package server;

import ui.FenetreServeur;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * serveur
 *
 * @author Gaël Burguès
 * @author Laurian Dufrechou
 * @author Lucàs Vabre
 */
public class Server extends Thread {

    /**
     * Le port de connexion du serveur
     */
    private final int PORT;

    /**
     * La liste des threads, connexion aux clients
     */
    private final ArrayList<ThreadServer> threads;

    /**
     * Interface graphique du serveur
     */
    private final FenetreServeur fenetre;

    /**
     * Crée un nouveau serveur qui n'est pas lié à une interface
     *
     * @param port Numéro de port du serveur
     */
    public Server(int port) {
        this.PORT = port;
        this.threads = new ArrayList<>();
        this.fenetre = null;
    }

    /**
     * Crée un nouveau serveur lié à une interface
     *
     * @param port    Numéros de port du serveur
     * @param fenetre Interface utilisateur du serveur
     */
    public Server(int port, FenetreServeur fenetre) {
        this.PORT = port;
        this.fenetre = fenetre;
        this.threads = new ArrayList<>();
    }

    @Override
    public void run() {
        try (ServerSocket serversocket = new ServerSocket(PORT)) {
            String msg = "Serveur démarré !";
            System.out.println(msg);
            fenetre.displayNewMessage(msg);

            while (true) {
                Socket socket = serversocket.accept(); // Une nouvelle connexion !

                // Messages
                String connexionMessage = String.format("Nouvelle connexion : %s\n", socket);
                System.out.printf(connexionMessage);
                if (fenetre != null) fenetre.displayNewMessage(connexionMessage);

                // Crée le thread d'écoute du client et l'ajoute dans la liste et le démarre
                ThreadServer thread = new ThreadServer(socket, threads, fenetre);
                threads.add(thread);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

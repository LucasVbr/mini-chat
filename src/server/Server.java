package server;

import ui.FenetreServeur;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{

    private final int PORT;
    private final ArrayList<ThreadServer> threads;
    private final FenetreServeur fenetre;

    public Server(int port) {
        this.PORT = port;
        this.threads = new ArrayList<>();

        this.fenetre = null;
    }

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
                Socket socket = serversocket.accept();

                String connexionMessage = String.format("Nouvelle connexion : %s\n", socket);
                System.out.printf(connexionMessage);
                fenetre.displayNewMessage(connexionMessage);

                ThreadServer thread = new ThreadServer(socket, threads, fenetre);

                threads.add(thread);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server serveur = new Server(4444);
        serveur.start();
    }
}

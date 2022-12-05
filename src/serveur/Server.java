package serveur;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private final int PORT;
    private final ArrayList<ThreadServer> threads;

    public Server(int port) {
        this.PORT = port;
        this.threads = new ArrayList<>();
    }

    public void start() {
        try (ServerSocket serversocket = new ServerSocket(PORT)) {
            System.out.println("Server is started...");
            while (true) {
                Socket socket = serversocket.accept();
                System.out.printf("Nouvelle connexion : %s\n", socket);

                ThreadServer thread = new ThreadServer(socket, threads);

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

package serveur;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Serveur {

    private static final int PORT = 4444;
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        // Connexion
        try {
            serverSocket = new ServerSocket(PORT); // Crée le serveur
            clientSocket = serverSocket.accept();  // On recherche un client
        } catch (IOException e) {
            System.out.printf("Erreur de connexion sur le port: %d\n", PORT);
            System.exit(-1);
        }

        // Un client a été trouvé
        System.out.println("Client connecté");

        boolean connectee = true;
        boolean reception = true;

        BufferedReader in = null;

        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message = null;
            do {
                if (reception) {
                    message = in.readLine();
                    System.out.printf("client > %s\n", message);
                } else {
                    System.out.print("serveur > ");
                    message = scan.nextLine();
                    out.println(message);
                }

                reception = !reception;
            } while (!message.equals("bye"));

            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

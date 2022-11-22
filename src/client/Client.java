package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        Socket serverSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        boolean ecoute = false;

        // Création des Sockets
        try {
            serverSocket = new Socket("localhost", 4444);
            System.out.println("Connecté au serveur");
            out = new PrintWriter(serverSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Destination unknown");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("now to investigate this IO issue");
            System.exit(-1);
        }

        String message = null;
        do {
            //ecoute d'un message
            if (ecoute) {
                message = in.readLine();
                System.out.printf("serveur > %s\n", message);
            } else {
                System.out.print("client > ");
                message = scan.nextLine();
                out.println(message);
            }
            ecoute = !ecoute;
        } while (!message.equals("bye"));

        //deconnexion
        //fermeture des Sockets
        out.close();
        in.close();
        serverSocket.close();
    }
}

package serveur;

import reseau.AES;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Scanner;

public class Serveur {

    private static final int PORT = 4444;
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        Key clientKey;

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

        ObjectInputStream in;
        ObjectOutputStream out;

        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());

            // On récupère la clé du client
            clientKey = (Key) in.readObject();

            System.out.println("Clé reçue");
            System.out.println(Arrays.toString(clientKey.getEncoded()));

            // Communication
            String message;
            byte[] messageCrypte;
            do {
                // Envoi du message
                messageCrypte = (byte[]) in.readObject();
                message = AES.decrypter(messageCrypte, clientKey);
                System.out.printf("client > %s -> %s\n", new String(messageCrypte, StandardCharsets.UTF_8), message);


                // Reception du message
                System.out.print("serveur > ");
                message = scan.nextLine();
                messageCrypte = AES.encrypter(message, clientKey);
                out.writeObject(messageCrypte);
            } while (!message.equals("bye"));

            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

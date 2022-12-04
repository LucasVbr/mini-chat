package serveur;

import reseau.RSA;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Scanner;

public class Serveur {

    private static final int PORT = 4444;
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        KeyPair serverKeyPairs = RSA.genererCle();
        PublicKey clientKey;

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
            clientKey = (PublicKey) in.readObject();
            out.writeObject(serverKeyPairs.getPublic());

            // Communication
            String message;
            byte[] messageCrypte;
            do {
                // Envoi du message
                messageCrypte = (byte[]) in.readObject();
                message = RSA.decrypter(messageCrypte, serverKeyPairs.getPrivate());
                System.out.printf("client > %s\n", message);


                // Reception du message
                System.out.print("serveur > ");
                message = scan.nextLine();
                messageCrypte = RSA.encrypter(message, clientKey);
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

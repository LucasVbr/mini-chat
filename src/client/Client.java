package client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.rmi.UnknownHostException;
import java.security.Key;
import java.util.Arrays;
import java.util.Scanner;

import reseau.AES;

public class Client {

    public static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        Socket serverSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        // Clé de chiffrage
        Key key = AES.genererCle();
        AES.sauvegarderCle(key);

        // Création des Sockets
        try {
            serverSocket = new Socket("localhost", 4444);
            System.out.println("Connecté au serveur");
            out = new ObjectOutputStream(serverSocket.getOutputStream());
            in = new ObjectInputStream(serverSocket.getInputStream());

            // On envoie la clé de chiffrage
            out.writeObject(key);

            System.out.println("Clé envoyé");
            System.out.println(Arrays.toString(key.getEncoded()));

        } catch (UnknownHostException e) {
            System.out.println("Destination unknown");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("now to investigate this IO issue");
            System.exit(-1);
        }

        // Communication
        String message;
        byte[] messageCrypte;
        try {
            do {
                // Envoi du message
                System.out.print("client > ");
                message = scan.nextLine();
                messageCrypte = AES.encrypter(message, key);
                out.writeObject(messageCrypte);

                // Reception du message
                messageCrypte = (byte[]) in.readObject();
                message = AES.decrypter(messageCrypte, key);
                System.out.printf("serveur > %s -> %s\n", new String(messageCrypte, StandardCharsets.UTF_8), message);
            } while (!message.equals("bye"));

            out.close();
            in.close();
            serverSocket.close();
        }catch (IOException | ClassNotFoundException e) {
           e.printStackTrace();
        }
    }
}

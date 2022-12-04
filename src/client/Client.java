package client;

import java.io.*;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Scanner;

import reseau.RSA;

public class Client {

    public static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        Socket serverSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        // Clé de chiffrage
        KeyPair clientKeyPair = RSA.genererCle();
        PublicKey serverKey = null;

        // Création des Sockets
        try {
            serverSocket = new Socket("localhost", 4444);
            System.out.println("Connecté au serveur");
            out = new ObjectOutputStream(serverSocket.getOutputStream());
            in = new ObjectInputStream(serverSocket.getInputStream());

            // On envoie la clé de chiffrage
            out.writeObject(clientKeyPair.getPublic());

            serverKey = (PublicKey) in.readObject();

        } catch (UnknownHostException e) {
            System.out.println("Destination unknown");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("now to investigate this IO issue");
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Communication
        String message;
        byte[] messageCrypte;
        try {
            do {
                // Envoi du message
                System.out.print("client > ");
                message = scan.nextLine();
                messageCrypte = RSA.encrypter(message, serverKey);
                out.writeObject(messageCrypte);

                // Reception du message
                messageCrypte = (byte[]) in.readObject();
                message = RSA.decrypter(messageCrypte, clientKeyPair.getPrivate());
                System.out.printf("serveur > %s\n", message);
            } while (!message.equals("bye"));

            out.close();
            in.close();
            serverSocket.close();
        }catch (IOException | ClassNotFoundException e) {
           e.printStackTrace();
        }
    }
}

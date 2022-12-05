package serveur;

import utils.RSA;

import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Objects;


public class ThreadServer extends Thread {

    private final ArrayList<ThreadServer> threads;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final KeyPair serverKeys;
    private PublicKey clientKey;
    private String clientPseudo;

    public ThreadServer(Socket socket, ArrayList<ThreadServer> threads) throws IOException {
        this.threads = threads;
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

    public void exchangeKeys() throws IOException, ClassNotFoundException {
        // Attend la clé du client
        clientKey = (PublicKey) inputStream.readObject();

        // Envoie sa clé
        outputStream.writeObject(serverKeys.getPublic());
    }

    public String getMessage() throws IOException, ClassNotFoundException {
        byte[] messageCrypte = (byte[]) inputStream.readObject();
        return RSA.decrypter(messageCrypte, serverKeys.getPrivate());
    }

    public void sendMessage(String message, boolean log) throws IOException {
        byte[] messageCrypte = RSA.encrypter(message, clientKey);
        outputStream.writeObject(messageCrypte);
        if (log) System.out.println(message);
    }

    public void sendMessageToEveryone(String message, boolean log) throws IOException {
        for (ThreadServer thread : threads) {
            thread.sendMessage(message, false);
        }
        if (log) System.out.println(message);
    }
}

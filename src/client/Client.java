package client;

import utils.RSA;
import utils.ResolutionDeNom;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    private final String SERVER_IP;
    private final int SERVER_PORT;
    private final Scanner scanner;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private KeyPair clientKeys;
    private PublicKey serverKey;

    private String pseudo;

    public Client(String ip, int port) {
        this.SERVER_IP = ResolutionDeNom.getIPAddress(ip);
        this.SERVER_PORT = port;
        this.scanner = new Scanner(System.in);
        this.clientKeys = RSA.genererCle();
    }

    public void start() {
        System.out.print("Saisir un pseudo: ");
        pseudo = scanner.nextLine();

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            exchangeKeys();

            sendMessage(pseudo);

            // Ecoute du serveur
            ListenThread threadClient = new ListenThread(this);
            threadClient.start();

            // Ecoute de l'entrée du clavier
            System.out.println("Tappez 'bye' pour quitter\n");
            String message;
            do {
                message = scanner.nextLine();
                sendMessage(message);
            } while (!Objects.equals(message, "bye"));

        } catch (ConnectException e) {
            System.err.println("Serveur non trouvé");
        }catch (EOFException e) {
            System.err.println("Connexion perdue");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exchangeKeys() throws IOException, ClassNotFoundException {
        // Envoie sa clé
        outputStream.writeObject(clientKeys.getPublic());

        // Attend la clé du serveur
        serverKey = (PublicKey) inputStream.readObject();
    }

    public void sendMessage(String message) throws IOException {
        byte[] messageCrypte = RSA.encrypter(message, serverKey);
        outputStream.writeObject(messageCrypte);
    }

    public String getMessage() throws IOException, ClassNotFoundException {
        byte[] messageCrypte = (byte[]) inputStream.readObject();
        return RSA.decrypter(messageCrypte, clientKeys.getPrivate());
    }


    public static void main(String[] args) {
        Client client = new Client("localhost", 4444);
        client.start();
    }
}

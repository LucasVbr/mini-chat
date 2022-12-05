package client;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

/**
 * Thread for clients
 */
public class ListenThread extends Thread {

    private final Client client;

    public ListenThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {

        try {
            String message;
            while (true) {
                message = client.getMessage();
                System.out.println(message);
            }
        } catch (SocketException ignored) {
            System.out.println("Vous avez quitté le salon");
        } catch (EOFException ignored) {
            System.err.println("Connexion perdue");
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

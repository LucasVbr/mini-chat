package client;

import ui.FenetreClient;
import ui.FenetreErreur;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

/**
 * Thread for clients
 */
public class ListenThread extends Thread {

    private final Client client;
    private final FenetreClient fenetre;

    public ListenThread(Client client) {
        this.client = client;
        this.fenetre = null;
    }

    public ListenThread(Client client, FenetreClient fenetre) {
        this.client = client;
        this.fenetre = fenetre;
    }

    @Override
    public void run() {

        try {
            String message;
            while (true) {
                message = client.getMessage();
                System.out.println(message);
                if (fenetre != null) {
                    fenetre.displayNewMessage(message);
                }
            }
        } catch (SocketException ignored) {
            System.out.println("Vous avez quitté le salon");
        } catch (EOFException ignored) {
            System.err.println("Connexion perdue");
            if (fenetre != null) {
                new FenetreErreur("Connexion perdue", fenetre);
                fenetre.deconnexion();
            }
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

/*
 * ListenThread.java, 06/12/2022
 * INU Champollion, 2022-2023
 * pas de copyright, aucun droits
 */

package client;

import ui.FenetreClient;
import ui.FenetreErreur;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

/**
 * Écoute des messages du serveur
 *
 * @author Gaël Burguès
 * @author Laurian Dufrechou
 * @author Lucàs Vabre
 */
public class ListenThread extends Thread {

    /** Instance du client */
    private final Client client;

    /** Interface utilisateur du client */
    private final FenetreClient fenetre;

    /**
     * Crée un nouveau thread
     *
     * @param client Le client lié à cette écoute
     */
    public ListenThread(Client client) {
        this.client = client;
        this.fenetre = null;
    }

    /**
     * Object d'ecoute avec fenetre
     *
     * @param client  client associe à l'ecoute
     * @param fenetre fenetreClient associe à l'ecoute
     */
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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

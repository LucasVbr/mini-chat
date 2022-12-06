/*
 * ResolutionDeNom.java, 06/12/2022
 * INU Champollion, 2022-2023
 * pas de copyright, aucun droits
 */

package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utilitaire à la gestion des adresses IP.
 *
 * @author Gaël Burguès
 * @author Laurian Dufrechou
 * @author Lucàs Vabre
 */
public class ResolutionDeNom {

    /**
     * Convertis une URI en on adresse IP
     *
     * @param uri Adresse URI à convertir
     * @return L'adresse IP correspondante à l'URI
     */
    public static String getIPAddress(String uri) {
        String address = null;
        try {
            address = InetAddress.getByName(uri).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
    }
}

/*
 * AES.java, 06/12/2022
 * INU Champollion, 2022-2023
 * pas de copyright, aucun droits
 */

package utils;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * Gestion du cryptage AES(DES)
 *
 * @author Gaël Burguès
 * @author Laurian Dufrechou
 * @author Lucàs Vabre
 */
public class AES {

    /** Nom du fichier de sauvegarde de la clé. */
    private static final String SAVE_FILE = "key.sav";

    /**
     * Génère une clé DES et la renvoie.
     *
     * @return La clé générée
     */
    public static Key genererCle() {
        Key key = null;

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            key = keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }


    /**
     * Crypte un message avec une clé.
     *
     * @param message La chaîne de caractère à crypter
     * @param cle     La clé de cryptage
     * @return Le message crypté sous forme d'un tableau d'octets
     */
    public static byte[] encrypter(String message, Key cle) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, cle);
            return cipher.doFinal(message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Décrypte un message à partir d'un tableau d'octet et de la clé de cryptage.
     *
     * @param messageCrypte Le message crypté
     * @param cle           La clé de cryptage
     * @return Le message une fois décrypté
     */
    public static String decrypter(byte[] messageCrypte, Key cle) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, cle);
            return new String(cipher.doFinal(messageCrypte), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Enregistre la clé dans le fichier {@link #SAVE_FILE}.
     *
     * @param cle La clé à sauvegarder
     */
    public static void sauvegarderCle(Key cle) {

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            objectOutputStream.writeObject(cle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Charge une clé à partir du fichier de sauvegarde {@link #SAVE_FILE}.
     *
     * @return La clé une fois chargée
     */
    public static Key chargerCle() {
        Key key;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            key = (Key) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return key;
    }
}

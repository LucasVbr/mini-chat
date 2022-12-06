/*
 * RSA.java, 06/12/2022
 * INU Champollion, 2022-2023
 * pas de copyright, aucun droits
 */

package utils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * Gestion du cryptage RSA.
 *
 * @author Gaël Burguès
 * @author Laurian Dufrechou
 * @author Lucàs Vabre
 */
public class RSA {

    /**
     * Génère une paire de clés RSA et nous les renvoie.
     *
     * @return La paire de clé générée (publique et privée)
     */
    public static KeyPair genererCle() {
        KeyPair keyPair = null;

        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
            keyGenerator.initialize(2048);

            keyPair = keyGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyPair;
    }

    /**
     * Crypte un message avec la clé publique.
     *
     * @param message   La chaîne de caractère à crypter
     * @param publicKey La clé de cryptage
     * @return Le message crypté sous forme d'un tableau d'octets
     */
    public static byte[] encrypter(String message, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Décrypte un message à partir d'un tableau d'octet et de la clé privée.
     *
     * @param message Le message crypté
     * @param privateKey La clé privée de cryptage
     * @return Le message une fois décrypté
     */
    public static String decrypter(byte[] message, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(message), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package utils;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class AES {

    private static final String SAVE_FILE = "key.sav";

    /**
     * TODO
     *
     * @return
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
     * TODO
     *
     * @param msg
     * @param key
     * @return
     */
    public static byte[] encrypter(String msg, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(msg.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * TODO
     *
     * @param message
     * @param cle
     * @return
     */
    public static String decrypter(byte[] message, Key cle) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, cle);
            return new String(cipher.doFinal(message), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * TODO
     *
     * @param key
     */
    public static void sauvegarderCle(Key key) {

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            objectOutputStream.writeObject(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * TODO
     *
     * @return
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

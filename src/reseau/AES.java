package reseau;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;

public class AES {

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

    public static String decrypter(byte[] msg, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("DES") ;
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(msg), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encrypter(String msg, Key key){
        try {
            Cipher cipher = Cipher.getInstance("DES") ;
            cipher.init(Cipher.ENCRYPT_MODE, key) ;
            return cipher.doFinal(msg.getBytes());
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void sauvegarderCle(Key key) {

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("key.ser"))) {
            objectOutputStream.writeObject(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Key chargerCle() {
        Key key = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("key.ser"))) {
            key = (Key) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return key;
    }
}

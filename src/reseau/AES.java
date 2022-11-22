package reseau;

import javax.crypto.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.util.Arrays;

public class AES {

    public static byte[] genererCle() {
        byte[] data;
        byte[] result;
        byte[] original = null;
        try {
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            Key key = kg.generateKey();
            return key.getEncoded();
//            Cipher cipher = Cipher.getInstance("DES") ;
//            cipher.init(Cipher.ENCRYPT_MODE, key) ;
//            data = "Hello World".getBytes() ;
//            result = cipher.doFinal(data) ;
//            cipher.init(Cipher.DECRYPT_MODE, key) ;
//            original = cipher.doFinal(result) ;
//            System.out.println("Decrypted data :" + Arrays.toString(original)) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return original;
    }

    public static byte[] decrypter(byte[] msg, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("DES") ;
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(msg);
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

    public static void sauvegarderFichier(Key key) {
        Path fileName = Path.of("./cle.txt");
        Files.writeString(fileName, new String(key));
    }

    public static void main(String[] args) {

    }
}

package reseau;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class RSA {

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

    public static byte[] encrypter(String msg, PublicKey key){
        try {
            Cipher cipher = Cipher.getInstance("RSA") ;
            cipher.init(Cipher.ENCRYPT_MODE, key) ;
            return cipher.doFinal(msg.getBytes());
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypter(byte[] msg, PrivateKey key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA") ;
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(msg), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package usecase;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by Pedro Mendoza on 07-Jun-16.
 */
public class GetRandomString {

    public static String get(){
        int keyLen = 128;
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(keyLen);
            SecretKey secretKey = keyGen.generateKey();
            byte[] encoded = secretKey.getEncoded();
            return DatatypeConverter.printHexBinary(encoded).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return UUID.randomUUID().toString();
        }
    }
}

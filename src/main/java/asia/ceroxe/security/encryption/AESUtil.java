package asia.ceroxe.security.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class AESUtil {

    private byte[] keyBytes;

    private SecretKey key;
    private Cipher enCipher;
    private Cipher deCipher;

    public AESUtil(int keySize) {
        try {
            keyBytes = new byte[keySize / 8];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(keyBytes);

            this.key = new SecretKeySpec(keyBytes, "AES");
            this.enCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            this.enCipher.init(Cipher.ENCRYPT_MODE, key);
            this.deCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            this.deCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AESUtil(SecretKey key) {
        try {
            this.key = key;
            this.enCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            this.enCipher.init(Cipher.ENCRYPT_MODE, key);
            this.deCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            this.deCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized byte[] encrypt(byte[] data) {
        try {
            return enCipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized byte[] encrypt(byte[] data, int inputOffset, int inputLen) {
        try {
            return enCipher.doFinal(data, inputOffset, inputLen);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized byte[] decrypt(byte[] data) {
        try {
            return deCipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized byte[] decrypt(byte[] data, int inputOffset, int inputLen) {
        try {
            return deCipher.doFinal(data, inputOffset, inputLen);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getKeyBytes() {
        return keyBytes;
    }

    public SecretKey getKey() {
        return key;
    }
}

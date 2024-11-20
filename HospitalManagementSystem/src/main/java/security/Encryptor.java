/**
 * The Encryptor class provides utility methods for encrypting and decrypting strings using the AES algorithm.
 * It uses a predefined key for encryption and decryption and provides methods for key handling.
 *
 * <p><strong>Note:</strong> The private key is hardcoded and must not be changed unless a secure key rotation mechanism is in place.</p>
 *
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 **/
package security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Encryptor {

    /** The predefined private key used for the encryption and decryption process. */
    private static final String GENERATED_KEY_FOR_PROJECT = "k18s6v4AKg412Ymnfiq/nQ==";

    /** The encryption algorithm used. */
    private static final String ALGORITHM = "AES";

    /** The transformation string for AES encryption and decryption. */
    private static final String TRANSFORMATION = "AES";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Encryptor() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Encrypts a given string using the predefined private key.
     *
     * @param data the plaintext string to be encrypted.
     * @return the encrypted string encoded in Base64 for easy storage or transmission.
     * @throws Exception if an error occurs during the encryption process.
     */
    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromString(GENERATED_KEY_FOR_PROJECT));
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts a Base64-encoded encrypted string using the predefined private key.
     *
     * @param encryptedData the encrypted string to be decrypted.
     * @return the decrypted plaintext string.
     * @throws Exception if an error occurs during the decryption process.
     */
    public static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromString(GENERATED_KEY_FOR_PROJECT));
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    /**
     * Converts a Base64-encoded string to a SecretKey.
     *
     * @param key the Base64-encoded string representing the secret key.
     * @return the SecretKey object created from the provided string.
     * @throws Exception if an error occurs while decoding or creating the SecretKey.
     */
    public static SecretKey getKeyFromString(String key) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }

    /**
     * Converts a SecretKey to a Base64-encoded string for secure storage or transmission.
     *
     * @param key the SecretKey object to be converted.
     * @return the Base64-encoded string representation of the SecretKey.
     */
    public static String keyToString(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}

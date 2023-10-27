package com.example.exchangerates.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.codec.EncodingException;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AesUtil {
    SecretKeySpec secretKey;
    final String ALGORITHM = "AES";

    /**
     * шифрование с помощтю ключа
     * @param data
     * @param secret
     * @return
     */
    public String encrypt(String data, String secret) {
        prepareSecreteKey(secret);

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return Base64.getEncoder().encodeToString(
                    cipher.doFinal(data.getBytes(StandardCharsets.UTF_8))
            );
        } catch (
                NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException e
        ) {
            log.error(e.getMessage());

            throw new EncodingException(e.getMessage());
        }
    }

    /**
     * дешифрование с помощью ключа
     * @param data
     * @param secret
     * @return
     */
    public String decrypt(String data, String secret) {
        prepareSecreteKey(secret);

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
        } catch (
                NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException e
        ) {
            log.error(e.getMessage());

            throw new EncodingException(e.getMessage());
        }
    }

    private void prepareSecreteKey(String key) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] byteKey = Arrays.copyOf(sha.digest(key.getBytes(StandardCharsets.UTF_8)), 16);

            secretKey = new SecretKeySpec(byteKey, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());

            throw new EncodingException(e.getMessage());
        }
    }
}
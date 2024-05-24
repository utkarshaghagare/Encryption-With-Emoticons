package com.example.ary.Service;

import com.example.ary.Util.EmojiMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.SecureRandom;

@Service
public class EncryptionService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private SecretKey secretKey;
    private final SecureRandom random = new SecureRandom();

    @PostConstruct
    public void init() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        secretKey = keyGen.generateKey();
    }

    public String encrypt(String message) throws Exception {
        byte[] iv = new byte[16];
        random.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        String encryptedMessage = Base64.toBase64String(encryptedBytes);
        String ivString = Base64.toBase64String(iv);

        return ivString + ":" + encryptedMessage;
    }
    public String decrypt(String encryptedMessage) throws Exception {
        String[] parts = encryptedMessage.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid encrypted message format");
        }

        String ivString = EmojiMapper.fromEmojiString(parts[0]);
        String encryptedString = EmojiMapper.fromEmojiString(parts[1]);

        System.out.println("Decoded IV String: " + ivString + " (Length: " + ivString.length() + ")");
        System.out.println("Decoded Encrypted String: " + encryptedString + " (Length: " + encryptedString.length() + ")");

        // Adding padding if necessary
        if (ivString.length() % 4 != 0) {
            ivString = ivString + "=".repeat((4 - ivString.length() % 4) % 4);
        }

        if (encryptedString.length() % 4 != 0) {
            encryptedString = encryptedString + "=".repeat((4 - encryptedString.length() % 4) % 4);
        }

        byte[] iv = Base64.decode(ivString);
        byte[] encryptedBytes = Base64.decode(encryptedString);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }





}



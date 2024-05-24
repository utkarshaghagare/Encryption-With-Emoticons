package com.example.ary;

import com.example.ary.Service.EncryptionService;
import com.example.ary.Util.EmojiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorld {

    @Autowired
    private EncryptionService encryptionService;

    @GetMapping("/encrypt-to-emoji")
    public String encryptToEmoji(@RequestParam String message) throws Exception {
        String encryptedMessage = encryptionService.encrypt(message);
        String[] parts = encryptedMessage.split(":");
        if (parts.length != 2) {
            // Handle invalid encrypted message format
            return "Invalid encrypted message format";
        }
        String iv = EmojiMapper.toEmojiString(parts[0]);
        String encrypted = EmojiMapper.toEmojiString(parts[1]);
        return iv + ":" + encrypted;
    }

    @GetMapping("/decrypt-from-emoji")
    public String decryptFromEmoji(@RequestParam String message) {
        try {
            String decryptedMessage = encryptionService.decrypt(message);
            return "Decrypted message: " + decryptedMessage;
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return "Decryption failed: " + e.getMessage(); // Return an error message
        }
    }

}

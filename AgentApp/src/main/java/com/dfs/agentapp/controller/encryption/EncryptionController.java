package com.dfs.agentapp.controller.encryption;

import com.dfs.agentapp.util.AESencryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EncryptionController {
    @Autowired
    private  AESencryption aesEncryption ;

    @PostMapping("/encrypt")
    public ResponseEntity<?> encryptData(@RequestBody String plainText) {
        try {
            // Encrypt the input data
            String encryptedData = aesEncryption.encryptwith256( plainText);
            return ResponseEntity.ok().body(encryptedData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Encryption failed: " + e.getMessage());
        }




    }
}

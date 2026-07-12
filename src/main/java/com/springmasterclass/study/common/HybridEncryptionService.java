package com.springmasterclass.study.common;

import com.springmasterclass.study.dto.record.EncryptedData;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class HybridEncryptionService {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public HybridEncryptionService() throws Exception {
        this.privateKey = readPrivateKey();
        this.publicKey = readPublicKey();
    }

    private PublicKey readPublicKey() throws Exception {
        try (PemReader reader = new PemReader(new FileReader("src/main/resources/keys/public_key.pem"))) {
            PemObject pem = reader.readPemObject();
            byte[] content = pem.getContent();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(content);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        }
    }

    private PrivateKey readPrivateKey() throws Exception {
        try (PemReader reader = new PemReader(new FileReader("src/main/resources/keys/private_key.pem"))) {
            PemObject pem = reader.readPemObject();
            byte[] content = pem.getContent();
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(content);
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        }
    }

    public EncryptedData encrypt(byte[] plaintext) throws Exception {
        // AES key 256-bit
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey aesKey = keyGen.generateKey();

        // IV 12 bytes
        byte[] iv = new byte[12];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        // AES-GCM encryption
        Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, gcmSpec);
        byte[] encryptedPayload = aesCipher.doFinal(plaintext);

        // RSA encrypt AES key
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedKey = rsaCipher.doFinal(aesKey.getEncoded());

        return new EncryptedData(
            Base64.getEncoder().encodeToString(encryptedKey),
            Base64.getEncoder().encodeToString(iv),
            Base64.getEncoder().encodeToString(encryptedPayload)
        );
    }

    public byte[] decrypt(EncryptedData encryptedData) throws Exception {
        byte[] encryptedKey = Base64.getDecoder().decode(encryptedData.encryptedKey());
        byte[] iv = Base64.getDecoder().decode(encryptedData.iv());
        byte[] encryptedPayload = Base64.getDecoder().decode(encryptedData.encryptedData());

        // RSA decrypt AES key
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] aesKeyBytes = rsaCipher.doFinal(encryptedKey);
        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");

        // AES-GCM decrypt
        Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey, gcmSpec);
        return aesCipher.doFinal(encryptedPayload);
    }
}
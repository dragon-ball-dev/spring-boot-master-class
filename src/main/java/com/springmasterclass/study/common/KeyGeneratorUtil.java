package com.springmasterclass.study.common;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class KeyGeneratorUtil {

    public static void main(String[] args) throws Exception {
        // 1. Tạo thư mục keys nếu chưa tồn tại
        File keysDir = new File("src/main/resources/keys");
        if (!keysDir.exists()) {
            boolean created = keysDir.mkdirs();
            if (created) {
                System.out.println("✅ Created folder: " + keysDir.getAbsolutePath());
            } else {
                System.err.println("❌ Can not create folder: " + keysDir.getAbsolutePath());
                return;
            }
        }

        // 2. Sinh cặp khóa RSA 2048-bit
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        // 3. Ghi public key
        File publicKeyFile = new File(keysDir, "public_key.pem");
        try (FileOutputStream fos = new FileOutputStream(publicKeyFile)) {
            fos.write("-----BEGIN PUBLIC KEY-----\n".getBytes());
            fos.write(Base64.getEncoder().encode(publicKey.getEncoded()));
            fos.write("\n-----END PUBLIC KEY-----\n".getBytes());
        }

        // 4. Ghi private key
        File privateKeyFile = new File(keysDir, "private_key.pem");
        try (FileOutputStream fos = new FileOutputStream(privateKeyFile)) {
            fos.write("-----BEGIN PRIVATE KEY-----\n".getBytes());
            fos.write(Base64.getEncoder().encode(privateKey.getEncoded()));
            fos.write("\n-----END PRIVATE KEY-----\n".getBytes());
        }

        System.out.println("✅ Keys generated and saved to: " + keysDir.getAbsolutePath());
        System.out.println("   - public_key.pem");
        System.out.println("   - private_key.pem");
    }
}
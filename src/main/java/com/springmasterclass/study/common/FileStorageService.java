package com.springmasterclass.study.common;

import com.springmasterclass.study.dto.record.EncryptedData;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class FileStorageService {
    @Value("${minio.bucket}")
    private String bucketName;

    private final MinioClient minioClient;
    private final HybridEncryptionService encryptionService;

    public FileStorageService(HybridEncryptionService encryptionService) {
        this.encryptionService = encryptionService;
        this.minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("minioadmin", "minioadmin")
                .build();
    }

    public String uploadEncryptedFile(MultipartFile file, String fileName) throws Exception {
        byte[] fileBytes = file.getBytes();
        EncryptedData encrypted = encryptionService.encrypt(fileBytes);

        // Ghép encryptedKey + iv + encryptedData (dùng dấu phân cách)
        String combined = encrypted.encryptedKey() + "||" 
                         + encrypted.iv() + "||" 
                         + encrypted.encryptedData();

        try (InputStream is = new ByteArrayInputStream(combined.getBytes())) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(is, combined.length(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
        }
        return fileName;
    }

    public byte[] downloadDecryptedFile(String fileName) throws Exception {
        try (InputStream is = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build())) {
            byte[] content = is.readAllBytes();
            String combined = new String(content);
            String[] parts = combined.split("\\|\\|");
            if (parts.length != 3) {
                throw new RuntimeException("Invalid encrypted file format");
            }
            EncryptedData encrypted = new EncryptedData(parts[0], parts[1], parts[2]);
            return encryptionService.decrypt(encrypted);
        }
    }
}
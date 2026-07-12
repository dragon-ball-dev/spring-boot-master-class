package com.springmasterclass.study.dto.record;

public record EncryptedData(
    String encryptedKey,
    String iv,
    String encryptedData
) {}
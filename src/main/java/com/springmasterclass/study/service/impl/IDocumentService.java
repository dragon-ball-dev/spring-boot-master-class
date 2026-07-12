package com.springmasterclass.study.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.springmasterclass.study.common.FileStorageService;
import com.springmasterclass.study.common.HybridEncryptionService;
import com.springmasterclass.study.dto.record.DocumentDto;
import com.springmasterclass.study.dto.record.EncryptedData;
import com.springmasterclass.study.entity.user.Document;
import com.springmasterclass.study.mapper.DocumentMapper;
import com.springmasterclass.study.repository.DocumentRepository;
import com.springmasterclass.study.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IDocumentService implements DocumentService {
    private final DocumentRepository documentRepository;
    private final HybridEncryptionService encryptionService;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DocumentMapper mapper = DocumentMapper.INSTANCE;

    // Inner record để serialize nội dung cần mã hóa
    private record DocumentContent(String title, String author, String department, String content) {}

    @Transactional
    public Document saveDocument(DocumentDto dto) throws Exception {
        // 1. Tạo đối tượng chứa nội dung cần mã hóa
        DocumentContent content = new DocumentContent(
            dto.title(),
            dto.author(),
            dto.department(),
            dto.content()
        );

        // 2. Chuyển thành JSON và mã hóa
        String json = objectMapper.writeValueAsString(content);
        EncryptedData encrypted = encryptionService.encrypt(json.getBytes());

        // 3. Tạo entity từ DTO (dùng MapStruct)
        Document document = mapper.toEntity(dto);
        document.setEncryptedPayload(objectMapper.writeValueAsString(encrypted));

        // 4. Xử lý file đính kèm (nếu có)
        if (dto.attachmentFile() != null && !dto.attachmentFile().isEmpty()) {
            String fileRef = fileStorageService.uploadEncryptedFile(
                dto.attachmentFile(),
                dto.title() + "_" + System.currentTimeMillis()
            );
            document.setFileReference(fileRef);
        }

        return documentRepository.save(document);
    }

    @Transactional(readOnly = true)
    public DocumentDto getDocument(Long id) throws Exception {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found"));

        // 1. Giải mã payload
        EncryptedData encrypted = objectMapper.readValue(
            document.getEncryptedPayload(),
            EncryptedData.class
        );
        byte[] decryptedBytes = encryptionService.decrypt(encrypted);
        String json = new String(decryptedBytes);

        // 2. Chuyển JSON thành DocumentContent (record)
        DocumentContent content = objectMapper.readValue(json, DocumentContent.class);

        // 3. Tạo DTO từ Entity (MapStruct) và bổ sung content đã giải mã
        DocumentDto dto = mapper.toDto(document);
        // Record không có setter, nên ta tạo mới với content được giải mã
        // Cách 1: dùng builder (nếu record có builder) hoặc tạo mới
        // Vì record là immutable, ta sẽ tạo mới
        return new DocumentDto(
            dto.title(),
            dto.author(),
            dto.department(),
            content.content(),
            null // không có file trong response
        );
    }
}
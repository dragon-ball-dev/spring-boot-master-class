package com.springmasterclass.study.controller;


import com.springmasterclass.study.dto.record.DocumentDto;
import com.springmasterclass.study.entity.user.Document;
import com.springmasterclass.study.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> createDocument(@ModelAttribute DocumentDto dto) throws Exception {
        Document saved = documentService.saveDocument(dto);
        return ResponseEntity.ok(Map.of(
            "id", saved.getId(),
            "message", "Tài liệu đã được lưu với mã hóa"
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<?> getDocument(@PathVariable Long id) throws Exception {
        DocumentDto dto = documentService.getDocument(id);
        return ResponseEntity.ok(dto);
    }
}
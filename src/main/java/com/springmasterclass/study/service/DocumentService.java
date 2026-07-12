package com.springmasterclass.study.service;

import com.springmasterclass.study.dto.record.DocumentDto;
import com.springmasterclass.study.entity.user.Document;

public interface DocumentService {

    Document saveDocument(DocumentDto dto) throws Exception;

    DocumentDto getDocument(Long id) throws Exception;
}

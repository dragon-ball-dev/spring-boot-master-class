package com.springmasterclass.study.dto.record;

import org.springframework.web.multipart.MultipartFile;

public record DocumentDto(
    String title,
    String author,
    String department,
    String content,                 // nội dung cần mã hóa
    MultipartFile attachmentFile    // file đính kèm
) {}
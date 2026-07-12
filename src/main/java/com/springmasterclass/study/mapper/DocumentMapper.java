package com.springmasterclass.study.mapper;

import com.springmasterclass.study.dto.record.DocumentDto;
import com.springmasterclass.study.entity.user.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocumentMapper {
    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    // Chỉ map các field đơn giản, không map content (vì content đã được mã hóa)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encryptedPayload", ignore = true)
    @Mapping(target = "fileReference", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Document toEntity(DocumentDto dto);

    // Mapping từ Entity sang DTO (không có content, vì content nằm trong encryptedPayload)
    @Mapping(target = "content", ignore = true)
    @Mapping(target = "attachmentFile", ignore = true)
    DocumentDto toDto(Document entity);
}
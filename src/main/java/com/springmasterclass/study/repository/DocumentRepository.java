package com.springmasterclass.study.repository;

import com.springmasterclass.study.entity.user.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
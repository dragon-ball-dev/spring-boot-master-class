package com.springmasterclass.study.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // Đánh dấu đây là một JPA Entity
@Table(name = "patients") // Ánh xạ với bảng patients trong DB

public class Patient {

    @Id // Khai báo Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(unique = true, length = 12)
    private String citizenId; // Căn cước công dân

    private LocalDateTime createdAt;

    // Hibernate yêu cầu No-args Constructor (Constructor không tham số)
    public Patient() {}
}
package com.springmasterclass.study.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String username;
    private String password;
}

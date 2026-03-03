package com.springmasterclass.study.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tl_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Giúp cho các bạn có thể insert vào User một các dễ dàng hơn thay vì các bạn phải contructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @Column(name = "address", length = 30)
    private String address;
    @Column(name = "phone", length = 10)
    private String phone;
    @Column(unique = true)
    private String email;
    private String username;
    private String password;
}

package com.springmasterclass.study.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "checkups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Checkup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String diagnosis;

    @ManyToOne(fetch = FetchType.LAZY) // BẮT BUỘC là LAZY để tái hiện N+1 (N)
    @JoinColumn(name = "patient_id")
    private Patient patient;

}
package com.springmasterclass.study.controller;

import com.springmasterclass.study.dto.record.PatientResponseDTO;
import com.springmasterclass.study.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/n-plus-one")
    public ResponseEntity<List<PatientResponseDTO>> getNPlusOne() {
        return ResponseEntity.ok(patientService.getPatientsWithNPlusOne());
    }
}
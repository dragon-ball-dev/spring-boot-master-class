package com.springmasterclass.study.service;

import com.springmasterclass.study.dto.record.PatientResponseDTO;

import java.util.List;

public interface PatientService {

    List<PatientResponseDTO> getPatientsWithNPlusOne();
}

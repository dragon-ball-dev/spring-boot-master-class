package com.springmasterclass.study.dto.record;

import java.util.List;

public record PatientResponseDTO(Long id, String fullName, List<String> diagnoses) {}
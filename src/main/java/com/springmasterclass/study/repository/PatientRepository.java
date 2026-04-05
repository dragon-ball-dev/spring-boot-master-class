package com.springmasterclass.study.repository;

import com.springmasterclass.study.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p JOIN FETCH p.checkups")
    List<Patient> findAllWithCheckUp();

    @EntityGraph(attributePaths = {"checkups"})
    @Query("SELECT p FROM Patient p")
    List<Patient> findAllWithCheckUpOptimized();

    @EntityGraph(attributePaths = {"checkups", "doctors"})
    Page<Patient> findAll(Pageable pageable);
}

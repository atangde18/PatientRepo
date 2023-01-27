package com.cerner.PatientApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cerner.PatientApp.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

}

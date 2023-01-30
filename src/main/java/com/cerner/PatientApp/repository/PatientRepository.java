package com.cerner.PatientApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cerner.PatientApp.model.Patient;

/**
 * Repository interface for Patient entity.
 * 
 * @author Akash Tangde
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
	
	/**
	 * Finds a patient by first name and last name.
	 * 
	 * @param firstName First name of the patient
	 * @param lastName Last name of the patient
	 * 
	 * @return The found patient or null if not found
	 */
	@Query("SELECT p FROM Patient p WHERE p.firstName = :firstName AND p.lastName = :lastName")
	Patient findByFirstName(@Param("firstName") String firstName, @Param("lastName") String lastName);

}

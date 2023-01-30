package com.cerner.PatientApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cerner.PatientApp.exceptions.RecordNotFoundException;
import com.cerner.PatientApp.model.Patient;
import com.cerner.PatientApp.service.ServiceClass;

/**
 * Controller class is a RESTful web service controller class, which exposes the
 * methods to perform CRUD operations on the Patient resource.
 * 
 * @author Akash Tangde
 */
@RestController
public class Controller {

	/**
	 * Autowired ServiceClass object, to access its methods
	 */
	@Autowired
	private ServiceClass serviceClass;

	/**
	 * POST method to create a new Patient resource
	 * 
	 * @param patient Patient object to be created
	 * @return ResponseEntity<Patient> The newly created Patient object and HTTP
	 *         status OK (200)
	 */
	@PostMapping("/patient")
	public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
		Patient newpatient = serviceClass.createPatient(patient);
		return new ResponseEntity<>(newpatient, HttpStatus.OK);
	}

	/**
	 * GET method to retrieve an existing Patient resource by id
	 * 
	 * @param id of the Patient to be retrieved
	 * @return ResponseEntity<Patient> The Patient object with the specified id and
	 *         HTTP status OK (200)
	 * @throws RecordNotFoundException when a Patient with the specified id is not
	 *                                 found
	 */
	@GetMapping("/patient/{id}")
	public ResponseEntity<Patient> getPatientId(@PathVariable Long id) throws RecordNotFoundException {
		Patient patient = serviceClass.getPatientById(id);
		return new ResponseEntity<>(patient, HttpStatus.OK);
	}

	/**
	 * GET method to retrieve an existing Patient resource by firstName and lastName
	 * 
	 * @param firstName First name of the patient
	 * @param lastName  Last name of the patient
	 * @return ResponseEntity containing the patient information if found, otherwise
	 *         throws RecordNotFoundException
	 * @throws RecordNotFoundException when no patient is found with given first
	 *                                 name and last name
	 */
	@GetMapping("/patient/name/firstname={firstName}&lastname={lastName}")
	public ResponseEntity<Patient> getPatientByName(@PathVariable String firstName, @PathVariable String lastName)
			throws RecordNotFoundException {
		Patient patient = serviceClass.getPatientByName(firstName, lastName);
		return new ResponseEntity<>(patient, HttpStatus.OK);
	}

	/**
	 * PUT method to update an existing Patient resource
	 * 
	 * @param id      id of the Patient to be updated
	 * @param patient Patient object with updated fields
	 * @return ResponseEntity<Patient> The updated Patient object and HTTP status OK
	 *         (200)
	 * @throws RecordNotFoundException when a Patient with the specified id is not
	 *                                 found
	 */
	@PutMapping("/patient/{id}")
	public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient)
			throws RecordNotFoundException {
		Patient updatedPatient = serviceClass.updatePatient(id, patient);
		return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
	}

	/**
	 * GET method to retrieve a list of all Patients
	 * 
	 * @return ResponseEntity<List<Patient>> List of all Patients and HTTP status OK
	 *         (200)
	 */
	@GetMapping("/patients")
	public ResponseEntity<List<Patient>> getAllPatients() {
		List<Patient> updatedPatient = serviceClass.getAllPatients();
		return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
	}

	/**
	 * DELETE method to delete an existing Patient resource
	 * 
	 * @param id id of the Patient to be deleted
	 * @return ResponseEntity<String> Confirmation message and HTTP status OK (200)
	 * @throws RecordNotFoundException when a Patient with the specified id is not
	 *                                 found
	 */
	@DeleteMapping("/patient/{id}")
	public ResponseEntity<String> deletePatient(@PathVariable Long id) throws RecordNotFoundException {
		String result = serviceClass.deletePatient(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}

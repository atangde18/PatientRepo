package com.cerner.PatientApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cerner.PatientApp.model.Patient;
import com.cerner.PatientApp.service.ServiceClass;

@RestController
public class Controller {

	@Autowired
	private ServiceClass serviceClass;

	@PostMapping("/patient")
	public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
		Patient newpatient = serviceClass.createPatient(patient);
		return new ResponseEntity<>(newpatient, HttpStatus.OK);
	}
	
	@GetMapping("/patient/{id}")
	public ResponseEntity<Patient> getPatient(@PathVariable Long id){
		Patient patient = serviceClass.getPatient(id);
		return new ResponseEntity<>(patient, HttpStatus.OK);
	}

}

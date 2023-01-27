package com.cerner.PatientApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cerner.PatientApp.model.Address;
import com.cerner.PatientApp.model.Patient;
import com.cerner.PatientApp.repository.PatientRepository;

@Service
public class ServiceClass {

	@Autowired
	private PatientRepository patientRepo;

	public Patient createPatient(Patient patient) {

		for (Address address : patient.getAddresses()) {
	        address.setPatient(patient);
	    }
	    return patientRepo.save(patient);

	}
	
	public Patient getPatient(Long id) {
		
		return patientRepo.findById(id).orElse(null);
		
	}
	

}

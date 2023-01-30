package com.cerner.PatientApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.cerner.PatientApp.exceptions.RecordNotFoundException;
import com.cerner.PatientApp.model.Address;
import com.cerner.PatientApp.model.Patient;
import com.cerner.PatientApp.repository.PatientRepository;

/**
 * ServiceClass is a service class to manage operations related to Patient
 * objects. It uses the PatientRepository to perform operations on the database.
 *
 * @author Akash Tangde
 */
@Service
public class ServiceClass {

	@Autowired
	private PatientRepository patientRepo;

	/**
	 * createPatient method is used to create a new patient record.
	 * 
	 * @param patient the patient object to be created.
	 * @return the saved patient object.
	 */
	public Patient createPatient(Patient patient) {

		for (Address address : patient.getAddresses()) {
			address.setPatient(patient);
		}
		return patientRepo.save(patient);

	}

	/**
	 * getPatient method is used to retrieve a patient record by id.
	 * 
	 * @param id the id of the patient to be retrieved.
	 * @return the patient object with the given id.
	 * @throws RecordNotFoundException if the patient with the given id does not
	 *                                 exist.
	 */
	public Patient getPatientById(Long id) throws RecordNotFoundException {

		Patient existingPatient = patientRepo.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Patient with Id : " + id + " does not exist"));

		return existingPatient;
	}

	/**
	 * 
	 * getPatientByName is used to retrieve a patient by first name and last name.
	 * 
	 * @param firstName the first name of the patient
	 * @param lastName  the last name of the patient
	 * @return the patient with the specified first name and last name
	 * @throws RecordNotFoundException if the patient with the specified first name
	 *                                 and last name does not exist
	 */

	public Patient getPatientByName(String firstName, String lastName) throws RecordNotFoundException {

		Patient existingPatient = patientRepo.findByFirstName(firstName, lastName);

		if (existingPatient == null)
			throw new RecordNotFoundException(
					"Patient with first name : " + firstName + " and last name : " + lastName + " does not exist");

		return existingPatient;
	}

	/**
	 * updatePatient method is used to update a patient record.
	 * 
	 * @param id the id of the patient to be updated.
	 * @param updatedPatient the updated patient object.
	 * @return the updated patient object.
	 * @throws RecordNotFoundException if the patient with the given id does not
	 *                                 exist.
	 */
	public Patient updatePatient(Long id, Patient updatedPatient) throws RecordNotFoundException {

		Patient existingPatient = patientRepo.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Patient with Id : " + id + " does not exist"));

		existingPatient.setFirstName(updatedPatient.getFirstName());
		existingPatient.setLastName(updatedPatient.getLastName());
		existingPatient.setDob(updatedPatient.getDob());
		existingPatient.setGender(updatedPatient.getGender());

		for (Address updatedAddress : updatedPatient.getAddresses()) {
			/** Find an existing address with the same addresstype */
			Address existingAddress = existingPatient.getAddresses().stream()
					.filter(a -> a.getAddressType().equals(updatedAddress.getAddressType())).findFirst().orElse(null);

			if (existingAddress != null) { /** Update the existing address */
				existingAddress.setStreet(updatedAddress.getStreet());
				existingAddress.setCity(updatedAddress.getCity());
				existingAddress.setState(updatedAddress.getState());
				existingAddress.setPinCode(updatedAddress.getPinCode());
			}
		}

		Patient savedPatient = patientRepo.save(existingPatient);

		return savedPatient;
	}

	/**
	 * getAllPatients method retrieves all patients from the database.
	 * 
	 * @return List<Patient> - A list of all patients in the database.
	 */
	public List<Patient> getAllPatients() {

		return patientRepo.findAll();

	}

	/**
	 * deletePatient deletes the patient record with the specified id
	 * 
	 * @param id
	 * @return @return a message indicating that the patient with the specified id
	 *         was deleted
	 * @throws RecordNotFoundException if a patient record with the specified id
	 *                                 does not exist
	 */

	public String deletePatient(Long id) throws RecordNotFoundException {
		try {
			patientRepo.deleteById(id);
			String result = "Patient with ID : " + id + " was deleted";
			return result;
		} catch (EmptyResultDataAccessException e) {
			throw new RecordNotFoundException("Could find record with Id : " + id);
		}

	}

}

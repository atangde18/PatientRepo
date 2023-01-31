package com.cerner.PatientApp.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Class representing a patient entity in the system
 * 
 * @author Akash Tangde
 */
@Entity
@Table(name = "patients")
public class Patient {

	/**
	 * Unique identifier for a patient
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long patientId;
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String gender;

	/**
	 * List of addresses associated with the patient
	 */
	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Address> addresses;

	public void setpatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	
	public void setAddresses(List<Address> addresses) {
		for(Address address: addresses) {
			address.setPatient(this);;
		}
		this.addresses = addresses;
	}

	public Patient() {
	}

}

package com.cerner.PatientApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Class representing a address entity in the system
 * 
 * @author Akash Tangde
 */
@Entity
@Table(name = "Address")
public class Address {

	/**
	 * Unique identifier for a patient
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;
	private String addressType;
	private String street;
	private String city;
	private String state;
	private String pinCode;
	private String teleNumber;

	/**
	 * Patient associated with the address
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "patient_id")
	@JsonBackReference
	private Patient patient;

	public void setId(Long addressId) {
		this.addressId = addressId;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public String getTeleNumber() {
		return teleNumber;
	}

	public void setTeleNumber(String teleNumber) {
		this.teleNumber = teleNumber;
	}

	
	
	public Address(Long id, String addressType, String street, String city, String state, String pinCode,
			String teleNumber, Patient patient) {
		super();
		this.addressId = id;
		this.addressType = addressType;
		this.street = street;
		this.city = city;
		this.state = state;
		this.pinCode = pinCode;
		this.teleNumber = teleNumber;
		this.patient = patient;
	}

	public Address() {
	}

}

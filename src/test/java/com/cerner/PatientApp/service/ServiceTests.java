package com.cerner.PatientApp.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.cerner.PatientApp.exceptions.CustomExceptionHandler;
import com.cerner.PatientApp.exceptions.ErrorResponse;
import com.cerner.PatientApp.exceptions.RecordNotFoundException;
import com.cerner.PatientApp.model.Address;
import com.cerner.PatientApp.model.Patient;
import com.cerner.PatientApp.repository.PatientRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
class ServiceTests {

	@MockBean
	private PatientRepository patientRepo;

	@Autowired
	private ServiceClass patientService;

	@Test
	public void testCreatePatient() {

		String dateString = "2023-01-27";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(dateString, formatter);

		Patient patient = new Patient();
		patient.setpatientId(null);
		patient.setFirstName("Gt");
		patient.setLastName("T");
		patient.setDob(localDate);
		patient.setGender("Female");
		List<Address> addresses = new ArrayList<Address>();
		Address address1 = new Address(null, "home", "mg", "mj", "MH", "4322", "84234446", patient);
		address1.setPatient(patient);
		Address address2 = new Address(null, "office", "mg", "mj", "MH", "4322", "94234446", patient);
		address2.setId(null);
		addresses.add(address1);
		addresses.add(address2);
		patient.setAddresses(addresses);

		// Create a mock for the patient repository
		when(patientRepo.save(patient)).thenReturn(patient);

		// Call the method under test
		Patient result = patientService.createPatient(patient);

		// Verify that the method was called on the mock repository
		verify(patientRepo).save(patient);

		// Assert that the result is what we expect
		assertEquals(patient, result);
		for (Address address : result.getAddresses()) {
			assertEquals(patient, address.getPatient());
		}
	}

	@Test
	public void testGetPatientById() throws RecordNotFoundException {
		// Given
		Long patientId = (long) 1;

		String dateString = "1999-01-01";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(dateString, formatter);

		Patient expectedpatient = new Patient();
		expectedpatient.setFirstName("At");
		expectedpatient.setLastName("T");
		expectedpatient.setDob(localDate);
		expectedpatient.setGender("Female");
		List<Address> addresses = new ArrayList<Address>();
		Address address1 = new Address(null, "home", "mg", "mj", "MH", "4322", "84234446", expectedpatient);
		address1.setPatient(expectedpatient);
		Address address2 = new Address(null, "office", "mj", "mj", "MH", "4322", "94234446", expectedpatient);
		address2.setId(null);
		addresses.add(address1);
		addresses.add(address2);
		expectedpatient.setAddresses(addresses);

		when(patientRepo.findById(patientId)).thenReturn(Optional.of(expectedpatient));

		// When
		Patient actualPatient = patientService.getPatientById(patientId);

		// Then
		assertAll(() -> assertEquals(actualPatient.getFirstName(), expectedpatient.getFirstName()),
				() -> assertEquals(actualPatient.getLastName(), expectedpatient.getLastName()),
				() -> assertEquals(actualPatient.getDob(), expectedpatient.getDob()),
				() -> assertEquals(actualPatient.getGender(), expectedpatient.getGender()));

		for (Address actualAddress : actualPatient.getAddresses()) {
			Address expectedAddress = expectedpatient.getAddresses().stream()
					.filter(a -> a.getAddressType().equals(actualAddress.getAddressType())).findFirst().orElse(null);
			assertEquals(actualAddress.getAddressType(), expectedAddress.getAddressType());
			assertEquals(actualAddress.getCity(), expectedAddress.getCity());
			assertEquals(actualAddress.getPinCode(), expectedAddress.getPinCode());
			assertEquals(actualAddress.getState(), expectedAddress.getState());
			assertEquals(actualAddress.getStreet(), expectedAddress.getStreet());
		}
		// assertEquals(expectedpatient, actualPatient);
	}

	@Test
	public void testGetPatientById_RecordNotFoundException() {
		// Given
		Long patientId = 1L;

		when(patientRepo.findById(patientId)).thenReturn(Optional.empty());

		// When
		RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
			patientService.getPatientById(patientId);
		});
		CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
		ResponseEntity<ErrorResponse> response = customExceptionHandler.handleException(exception);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Patient with Id : " + patientId + " does not exist", response.getBody().getMessage());
	}

	@Test
	public void testGetPatientByName() throws RecordNotFoundException {
		// Arrange

		String firstName = "John";
		String lastName = "Doe";

		String dateString = "1999-01-01";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(dateString, formatter);

		Patient expectedpatient = new Patient();
		expectedpatient.setFirstName(firstName);
		expectedpatient.setLastName(lastName);
		expectedpatient.setDob(localDate);
		expectedpatient.setGender("Female");
		List<Address> addresses = new ArrayList<Address>();
		Address address1 = new Address(null, "home", "mg", "mj", "MH", "4322", "84234446", expectedpatient);
		address1.setPatient(expectedpatient);
		Address address2 = new Address(null, "office", "mj", "mj", "MH", "4322", "94234446", expectedpatient);
		address2.setId(null);
		addresses.add(address1);
		addresses.add(address2);
		expectedpatient.setAddresses(addresses);

		Mockito.when(patientRepo.findByFirstName(firstName, lastName)).thenReturn(expectedpatient);

		// Act & Assert
		Patient returnedPatient = patientService.getPatientByName(firstName, lastName);

		assertEquals(expectedpatient, returnedPatient);
	}

	@Test
	public void testUpdatePatient_Success() throws RecordNotFoundException {
		// Given
		Long id = 1L;
		Patient existingPatient = new Patient();
		existingPatient.setpatientId(id);
		existingPatient.setFirstName("J");
		existingPatient.setLastName("D");
		existingPatient.setDob(LocalDate.of(2000, 01, 01));
		existingPatient.setGender("Male");
		List<Address> addresses = new ArrayList<>();
		Address address = new Address();
		address.setAddressType("Home");
		address.setStreet("Main St");
		address.setCity("LA");
		address.setState("CA");
		address.setPinCode("12345");
		addresses.add(address);
		existingPatient.setAddresses(addresses);
		when(patientRepo.findById(id)).thenReturn(Optional.of(existingPatient));
		when(patientRepo.save(existingPatient)).thenReturn(existingPatient);

		// When
		Patient updatedPatient = new Patient();
		updatedPatient.setFirstName("J");
		updatedPatient.setLastName("D");
		updatedPatient.setDob(LocalDate.of(2000, 01, 01));
		updatedPatient.setGender("Male");
		List<Address> updatedAddresses = new ArrayList<>();
		Address updatedAddress = new Address();
		updatedAddress.setAddressType("Home");
		updatedAddress.setStreet("Main St");
		updatedAddress.setCity("LA");
		updatedAddress.setState("CA");
		updatedAddress.setPinCode("12345");
		updatedAddresses.add(updatedAddress);
		updatedPatient.setAddresses(updatedAddresses);
		Patient result = patientService.updatePatient(id, updatedPatient);

		// Then
		assertNotNull(result);
		assertEquals("J", result.getFirstName());
		assertEquals("D", result.getLastName());
		assertEquals(LocalDate.of(2000, 01, 01), result.getDob());
		assertEquals("Male", result.getGender());
		assertEquals(1, result.getAddresses().size());
		assertEquals("Main St", result.getAddresses().get(0).getStreet());
		assertEquals("LA", result.getAddresses().get(0).getCity());
		assertEquals("CA", result.getAddresses().get(0).getState());
		assertEquals("12345", result.getAddresses().get(0).getPinCode());
		verify(patientRepo, times(1)).findById(id);
		verify(patientRepo, times(1)).save(existingPatient);
	}

	@Test
	public void whenValidId_thenPatientShouldBeDeleted() throws Exception {
		// given
		Long id = 1L;
		Patient existingPatient = new Patient();
		existingPatient.setpatientId(id);
		existingPatient.setFirstName("J");
		existingPatient.setLastName("D");
		existingPatient.setDob(LocalDate.of(2000, 01, 01));
		existingPatient.setGender("Male");
		List<Address> addresses = new ArrayList<>();
		Address address = new Address();
		address.setAddressType("Home");
		address.setStreet("Main St");
		address.setCity("LA");
		address.setState("CA");
		address.setPinCode("12345");
		addresses.add(address);
		existingPatient.setAddresses(addresses);
		existingPatient = patientRepo.save(existingPatient);

		// when
		String result = patientService.deletePatient(id);

		// then
		Optional<Patient> deletedPatient = patientRepo.findById(id);
		assertFalse(deletedPatient.isPresent());
		assertEquals("Patient with ID : " + id + " was deleted", result);
	}

	@Test
	public void testGetAllPatients_ShouldReturnAllPatients() {
		List<Patient> expectedPatients = new ArrayList<Patient>();

		Long id = 1L;
		Patient patient1 = new Patient();
		patient1.setpatientId(id);
		patient1.setFirstName("J");
		patient1.setLastName("D");
		patient1.setDob(LocalDate.of(2000, 01, 01));
		patient1.setGender("Male");
		List<Address> addresses1 = new ArrayList<>();
		Address address1 = new Address();
		address1.setAddressType("Home");
		address1.setStreet("Main St");
		address1.setCity("LA");
		address1.setState("CA");
		address1.setPinCode("12345");
		addresses1.add(address1);
		patient1.setAddresses(addresses1);

		Patient patient2 = new Patient();
		patient2.setFirstName("J");
		patient2.setLastName("D");
		patient2.setDob(LocalDate.of(2000, 01, 01));
		patient2.setGender("Male");
		List<Address> addresses2 = new ArrayList<>();
		Address address2 = new Address();
		address2.setAddressType("Home");
		address2.setStreet("Main St");
		address2.setCity("LA");
		address2.setState("CA");
		address2.setPinCode("12345");
		addresses2.add(address2);
		patient2.setAddresses(addresses2);

		expectedPatients.add(patient1);
		expectedPatients.add(patient2);

		when(patientRepo.findAll()).thenReturn(expectedPatients);

		List<Patient> actualPatients = patientService.getAllPatients();

		assertEquals(expectedPatients, actualPatients);
		verify(patientRepo, times(1)).findAll();
	}

}

package com.cerner.PatientApp.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cerner.PatientApp.model.Address;
import com.cerner.PatientApp.model.Patient;
import com.cerner.PatientApp.service.ServiceClass;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(value = PatientController.class)
class PatientControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ServiceClass serviceClass;
	
	@Test
	public void testCreatePatient() throws Exception {
		Patient patient = new Patient();
		patient.setpatientId(null);
		patient.setFirstName("Gt");
		patient.setLastName("T");
		patient.setDob(LocalDate.of(2000, 01, 01));
		patient.setGender("Female");
		List<Address> addresses = new ArrayList<Address>();
		Address address1 = new Address(null, "home", "mg", "mj", "MH", "4322", "84234446", patient);
		address1.setPatient(patient);
		Address address2 = new Address(null, "office", "mg", "mj", "MH", "4322", "94234446", patient);
		address2.setId(null);
		addresses.add(address1);
		addresses.add(address2);
		patient.setAddresses(addresses);

	    when(serviceClass.createPatient(any())).thenReturn(patient);

	    MvcResult result = mockMvc.perform(post("/patient").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(patient))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    Patient returnedPatient = objectMapper.readValue(result.getResponse().getContentAsString(), Patient.class);
	    assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
	    
	    assertAll(() -> assertEquals(patient.getFirstName(), returnedPatient.getFirstName()),
				() -> assertEquals( patient.getLastName(), returnedPatient.getLastName()),
				() -> assertEquals( patient.getDob(), returnedPatient.getDob()),
				() -> assertEquals( patient.getGender(), returnedPatient.getGender()));

		for (Address actualAddress : returnedPatient.getAddresses()) {
			Address expectedAddress = patient.getAddresses().stream()
					.filter(a -> a.getAddressType().equals(actualAddress.getAddressType())).findFirst().orElse(null);
			assertEquals(expectedAddress.getAddressType(), actualAddress.getAddressType());
			assertEquals( expectedAddress.getCity(), actualAddress.getCity());
			assertEquals(expectedAddress.getPinCode(), actualAddress.getPinCode());
			assertEquals( expectedAddress.getState(), actualAddress.getState());
			assertEquals(expectedAddress.getStreet(), actualAddress.getStreet());
		}
	    
	}
	
	@Test
	public void testGetPatientById() throws Exception {
		// arrange
		Long id = 1L;
		Patient patient = new Patient();
		patient.setpatientId(null);
		patient.setFirstName("Gt");
		patient.setLastName("T");
		patient.setDob(LocalDate.of(2000, 01, 01));
		patient.setGender("Female");
		List<Address> addresses = new ArrayList<Address>();
		Address address1 = new Address(null, "home", "mg", "mj", "MH", "4322", "84234446", patient);
		address1.setPatient(patient);
		Address address2 = new Address(null, "office", "mg", "mj", "MH", "4322", "94234446", patient);
		address2.setId(null);
		addresses.add(address1);
		addresses.add(address2);
		patient.setAddresses(addresses);
		when(serviceClass.getPatientById(id)).thenReturn(patient);

		// act & assert
		MvcResult result = mockMvc.perform(get("/patient/{id}", id)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());

		verify(serviceClass).getPatientById(id);
	}

	@Test
	public void testGetPatientByName() throws Exception {
		// arrange
		String firstName = "Gt";
		String lastName = "T";
		Patient patient = new Patient();
		patient.setpatientId(null);
		patient.setFirstName(firstName);
		patient.setLastName(lastName);
		patient.setDob(LocalDate.of(2000, 01, 01));
		patient.setGender("Female");
		List<Address> addresses = new ArrayList<Address>();
		Address address1 = new Address(null, "home", "mg", "mj", "MH", "4322", "84234446", patient);
		address1.setPatient(patient);
		Address address2 = new Address(null, "office", "mg", "mj", "MH", "4322", "94234446", patient);
		address2.setId(null);
		addresses.add(address1);
		addresses.add(address2);
		patient.setAddresses(addresses);
		when(serviceClass.getPatientByName(firstName, lastName)).thenReturn(patient);

		// act & assert
		MvcResult result = mockMvc
				.perform(get("/patient/name/firstname={firstName}&lastname={lastName}", firstName, lastName))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());

		verify(serviceClass).getPatientByName(firstName, lastName);
	}

	@Test
	public void testUpdatePatient() throws Exception {
		// arrange
		String firstName = "Gt";
		String lastName = "T";
		Patient patient = new Patient();
		patient.setpatientId(null);
		patient.setFirstName(firstName);
		patient.setLastName(lastName);
		patient.setDob(LocalDate.of(2000, 01, 01));
		patient.setGender("Female");
		List<Address> addresses = new ArrayList<Address>();
		Address address1 = new Address(null,"home", "mg", "mj", "MH", "4322", "84234446", patient);
		address1.setPatient(patient);
		Address address2 = new Address(null,"office", "mg", "mj", "MH", "4322", "94234446", patient);
		address2.setId(null);
		addresses.add(address1);
		addresses.add(address2);
		patient.setAddresses(addresses);
		Long id = (long) 1;
		when(serviceClass.updatePatient(eq(id), any())).thenReturn(patient);

		// act & assert
		MvcResult result = mockMvc.perform(put("/patient/{id}", id).contentType(MediaType.APPLICATION_JSON).content(asJsonString(patient)))
				.andExpect(status().isOk()).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
		
		verify(serviceClass, times(1)).updatePatient(eq(id), any());
	}
	
	@Test
	public void testGetAllPatients() throws Exception {
		Long id = 1L;
		Patient patient1 = new Patient();
		patient1.setpatientId(id);
		patient1.setFirstName("K");
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

		
	    List<Patient> expectedPatients = Arrays.asList(patient1, patient2);
	    
	    when(serviceClass.getAllPatients()).thenReturn(expectedPatients);
	    RequestBuilder requestBuilder = MockMvcRequestBuilders
	                                    .get("/patients")
	                                    .accept(MediaType.APPLICATION_JSON);
	    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	    MockHttpServletResponse response = result.getResponse();
	    assertEquals(HttpStatus.OK.value(), response.getStatus());
	    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
		List<Patient> actualPatients = objectMapper.readValue(response.getContentAsString(),
	                                            new TypeReference<List<Patient>>() {});
		
		for(Patient actualPatient: actualPatients) {
			
			Patient expectedPatient = expectedPatients.stream()
					.filter(a -> a.getFirstName().equals(actualPatient.getFirstName())).findFirst().orElse(null);
			
			assertAll(() -> assertEquals(expectedPatient.getFirstName(), actualPatient.getFirstName()),
					() -> assertEquals( expectedPatient.getLastName(), actualPatient.getLastName()),
					() -> assertEquals( expectedPatient.getDob(), actualPatient.getDob()),
					() -> assertEquals( expectedPatient.getGender(), actualPatient.getGender()));

			for (Address actualAddress : actualPatient.getAddresses()) {
				Address expectedAddress = expectedPatient.getAddresses().stream()
						.filter(a -> a.getAddressType().equals(actualAddress.getAddressType())).findFirst().orElse(null);
				assertEquals(expectedAddress.getAddressType(), actualAddress.getAddressType());
				assertEquals( expectedAddress.getCity(), actualAddress.getCity());
				assertEquals(expectedAddress.getPinCode(), actualAddress.getPinCode());
				assertEquals( expectedAddress.getState(), actualAddress.getState());
				assertEquals(expectedAddress.getStreet(), actualAddress.getStreet());
			}
		}
		
	    verify(serviceClass, times(1)).getAllPatients();
	}
	
	@Test
	public void testDeletePatient() throws Exception {
	    Long id = 1L;
	    String result = "Patient with ID : 1 was deleted";

	    when(serviceClass.deletePatient(id)).thenReturn(result);

	    RequestBuilder request = MockMvcRequestBuilders
	        .delete("/patient/" + id)
	        .accept(MediaType.APPLICATION_JSON);

	    MvcResult mvcResult = mockMvc.perform(request)
	        .andExpect(status().isOk())
	        .andReturn();

	    assertEquals(result, mvcResult.getResponse().getContentAsString());

	    verify(serviceClass, times(1)).deletePatient(id);
	}
	
	

	public static String asJsonString(final Object obj) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

package com.cerner.PatientApp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.cerner.PatientApp.model.Address;
import com.cerner.PatientApp.model.Patient;
import com.cerner.PatientApp.service.ServiceClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(value = PatientController.class)
class PatientControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ServiceClass serviceClass;

	@Test
	public void getTetPatientById() throws Exception {
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
		// System.out.println(response.getContentAsString());

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
		// System.out.println(response.getContentAsString());

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
		
		System.out.println("awdawdawdawdawd"+response.getContentAsString());
		
		verify(serviceClass, times(1)).updatePatient(eq(id), any());
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

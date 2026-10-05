package com.healthtrack.backend.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.healthtrack.backend.DAO.PatientDAO;
import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Patient;
import com.healthtrack.backend.Exception.ResourceNotFoundException;
import com.healthtrack.backend.Service.PatientService;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

	@InjectMocks
	private PatientService service;
	@Mock
	private PatientDAO patientdao;


	@Nested
	class CreateTests {

		@Test
		 void shouldSavePatient() {
			Patient patient = new Patient();
			patient.setName("Rocky");
			when(patientdao.registerPatient(patient)).thenReturn(patient);
			ResponseEntity<ResponseStructure<Patient>> responseEntity = service.registerPatient(patient);
			assertEquals("Rocky", responseEntity.getBody().getData().getName());
			verify(patientdao, times(1)).registerPatient(patient);
		}

		@Test
		 void shouldThrowNullPointerExceptionWhenSaveFails() {
			Patient patient = new Patient();
			when(patientdao.registerPatient(patient)).thenReturn(null);
			assertThrows(NullPointerException.class, () -> service.registerPatient(patient));
			verify(patientdao, times(1)).registerPatient(patient);
		}

	}

	@Nested
	class ReadTests {
		@Test
		 void shouldFindPatientByIdSuccessfully() {
			Patient patient = new Patient();
			patient.setId(1);
			when(patientdao.findPatientById(patient.getId())).thenReturn(Optional.of(patient));
			ResponseEntity<ResponseStructure<Patient>> responseEntity = service.findPatientById(patient.getId());
			assertEquals(1, responseEntity.getBody().getData().getId());
			verify(patientdao, times(1)).findPatientById(patient.getId());
		}

		@Test
		 void shouldThrowResourceNotFoundExceptionWHenPatientNotFound() {
			Patient patient = new Patient();
			patient.setId(99);
			when(patientdao.findPatientById(patient.getId())).thenReturn(Optional.empty());
			assertThrows(ResourceNotFoundException.class, () -> service.findPatientById(patient.getId()));
			verify(patientdao, times(1)).findPatientById(patient.getId());

		}

		@Test
		 void shouldReturnAllPatientsSuccessfully() {
			List<Patient> asList = Arrays.asList(new Patient(), new Patient());
			when(patientdao.findAllPatient()).thenReturn(asList);
			ResponseEntity<ResponseStructure<List<Patient>>> responseEntity = service.findAllPatients();
			assertEquals(2, responseEntity.getBody().getData().size());
			verify(patientdao, times(1)).findAllPatient();
		}

		@Test
		 void shouldThrowResourceNotFoundExceptionWhenPatientNotFound() {
			when(patientdao.findAllPatient()).thenReturn(Collections.emptyList());
			assertThrows(ResourceNotFoundException.class, () -> service.findAllPatients());
			verify(patientdao, times(1)).findAllPatient();
		}
	}

	@Nested
	class UpdateTests {
		@Test
		 void shouldUpdatePatientSuccessfully() {
			Patient existed = new Patient();
			existed.setId(1);
			Patient update = new Patient();
			update.setId(1);
			update.setName("Captain");
			when(patientdao.findPatientById(1)).thenReturn(Optional.of(existed));
			when(patientdao.updatePatient(update)).thenReturn(update);
			ResponseEntity<ResponseStructure<Patient>> responseEntity = service.updatePatient(update, update.getId());
			Patient data = responseEntity.getBody().getData();
			assertEquals("Captain", data.getName());
			verify(patientdao, times(1)).updatePatient(update);
			verify(patientdao, times(1)).findPatientById(1);

		}

		@Test
		 void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistingPatient() {
			Patient patient = new Patient();
			patient.setId(99);
			when(patientdao.findPatientById(patient.getId())).thenReturn(Optional.empty());
			when(patientdao.updatePatient(patient)).thenReturn(patient);
			assertThrows(ResourceNotFoundException.class, () -> service.updatePatient(patient, patient.getId()));
			verify(patientdao, times(1)).findPatientById(patient.getId());

		}
	}

	@Nested
	class DeleteTests {
		@Test
		 void shouldDeletePatientSuccessfully() {
			Patient patient = new Patient();
			patient.setId(1);
			when(patientdao.findPatientById(patient.getId())).thenReturn(Optional.of(patient));
			ResponseEntity<ResponseStructure<String>> responseEntity = service.deletePatientById(patient.getId());

			assertEquals(200, responseEntity.getBody().getStatusCode());
			verify(patientdao, times(1)).findPatientById(patient.getId());
			verify(patientdao, times(1)).deletePatientById(patient.getId());
		}

		@Test
		 void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistingPatient() {
			Patient patient = new Patient();
			patient.setId(99);
			when(patientdao.findPatientById(patient.getId())).thenReturn(Optional.empty());
			assertThrows(ResourceNotFoundException.class, () -> service.deletePatientById(patient.getId()));
			verify(patientdao, times(1)).findPatientById(patient.getId());
			verify(patientdao, times(0)).deletePatientById(patient.getId());
		}
	}

}

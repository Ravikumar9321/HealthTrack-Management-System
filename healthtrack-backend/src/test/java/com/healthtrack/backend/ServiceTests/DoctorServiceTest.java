package com.healthtrack.backend.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import com.healthtrack.backend.DAO.DoctorDAO;
import com.healthtrack.backend.DTO.DoctorRequest;
import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Doctor;
import com.healthtrack.backend.Exception.ResourceNotFoundException;
import com.healthtrack.backend.Service.DoctorService;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {
	@Mock
	private DoctorDAO doctordao;
	@InjectMocks
	private DoctorService service;

	

	@Nested
	class CreateDoctorTests {

	    @Test
	    void shouldRegisterDoctorSuccessfully() {
	        DoctorRequest request = new DoctorRequest("Dr. Strange", "strange@gmail.com", "Cardiology", "strange123", null);

	        Doctor doctor = new Doctor();
	        doctor.setId(1);
	        doctor.setName("Dr. Strange");
	        doctor.setEmail("strange@gmail.com");
	        doctor.setSpecialization("Cardiology");

	        when(doctordao.findDoctorByEmail(request.email())).thenReturn(Optional.empty());
	        when(doctordao.registerDoctor(any(Doctor.class))).thenReturn(doctor);

	        ResponseEntity<ResponseStructure<Doctor>> responseEntity = service.registerDoctor(request);

	        Doctor data = responseEntity.getBody().getData();
	        assertEquals(1, data.getId());
	        assertEquals("Dr. Strange", data.getName());
	        verify(doctordao, times(1)).registerDoctor(any(Doctor.class));
	    }

	    @Test
	    void shouldThrowIllegalArgumentExceptionWhenEmailMissing() {
	        DoctorRequest request = new DoctorRequest("Dr. Strange", null, "Cardiology", "strange123", null);

	        assertThrows(IllegalArgumentException.class, () -> service.registerDoctor(request));
	        verify(doctordao, never()).registerDoctor(any(Doctor.class));
	    }

	    @Test
	    void shouldThrowDataIntegrityViolationExceptionWhenEmailExists() {
	        DoctorRequest request = new DoctorRequest("Dr. Strange", "strange@gmail.com", "Cardiology", "strange123", null);

	        when(doctordao.findDoctorByEmail(request.email())).thenReturn(Optional.of(new Doctor()));

	        assertThrows(DataIntegrityViolationException.class, () -> service.registerDoctor(request));
	        verify(doctordao, never()).registerDoctor(any(Doctor.class));
	    }
	}


	@Nested
	class ReadTests {
		@Test
		 void shouldReturnDoctorByIdSuccessfully() {
			Doctor doctor = new Doctor();
			doctor.setId(1);
			when(doctordao.findDoctorById(doctor.getId())).thenReturn(Optional.of(doctor));
			ResponseEntity<ResponseStructure<Doctor>> responseEntity = service.findDoctorById(doctor.getId());
			assertEquals(1, responseEntity.getBody().getData().getId());
			verify(doctordao, times(1)).findDoctorById(doctor.getId());
		}

		@Test
		 void shouldThrowResourceNotFoundExceptionWhenDoctorNotFound() {
			Doctor doctor = new Doctor();
			doctor.setId(99);
			when(doctordao.findDoctorById(doctor.getId())).thenReturn(Optional.empty());
			assertThrows(ResourceNotFoundException.class, () -> service.findDoctorById(doctor.getId()));
			verify(doctordao, times(1)).findDoctorById(doctor.getId());

		}

		@Test
		 void shouldReturnAllDoctorsSuccessfully() {
			List<Doctor> asList = Arrays.asList(new Doctor(), new Doctor());
			when(doctordao.findAllDoctor()).thenReturn(asList);
			ResponseEntity<ResponseStructure<List<Doctor>>> responseEntity = service.findAllDoctors();
			assertEquals(2, responseEntity.getBody().getData().size());
			verify(doctordao, times(1)).findAllDoctor();
		}

		@Test
		public void shouldThrowResourceNotFoundExceptionWhenDoctorNoFound() {
			when(doctordao.findAllDoctor()).thenReturn(Collections.emptyList());
			assertThrows(ResourceNotFoundException.class, () -> service.findAllDoctors());
			verify(doctordao, times(1)).findAllDoctor();
		}
	}

	@Nested
	class UpdateTests {
		@Test
		 void shouldUpdateDoctorSuccessfully() {
			Doctor existed = new Doctor();
			existed.setId(1);
			existed.setName("Rocky");
			Doctor update = new Doctor();
			update.setId(1);
			update.setName("Captain");
			when(doctordao.findDoctorById(existed.getId())).thenReturn(Optional.of(existed));
			when(doctordao.updateDoctor(update)).thenReturn(update);
			ResponseEntity<ResponseStructure<Doctor>> responseEntity = service.updateDoctor(update, update.getId());
			assertEquals("Captain", responseEntity.getBody().getData().getName());
			verify(doctordao, times(1)).updateDoctor(update);
			verify(doctordao, times(1)).findDoctorById(existed.getId());
		}

		@Test
		 void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistingDoctor() {
			Doctor existed = new Doctor();
			existed.setId(1);
			existed.setName("Rocky");
			Doctor update = new Doctor();
			update.setId(1);
			update.setName("Captain");
			when(doctordao.findDoctorById(existed.getId())).thenReturn(Optional.empty());
			when(doctordao.updateDoctor(update)).thenReturn(update);
			assertThrows(ResourceNotFoundException.class, () -> service.updateDoctor(update, update.getId()));
			verify(doctordao, times(1)).findDoctorById(existed.getId());

		}
	}

	@Nested
	class DeleteTests {
		@Test
		 void shouldDeleteDoctorSuccessfully() {
			Doctor doctor = new Doctor();
			doctor.setId(1);
			when(doctordao.findDoctorById(doctor.getId())).thenReturn(Optional.of(doctor));
			ResponseEntity<ResponseStructure<String>> responseEntity = service.deleteDoctorById(doctor.getId());
			assertEquals(200, responseEntity.getBody().getStatusCode());
			verify(doctordao, times(1)).findDoctorById(doctor.getId());
			verify(doctordao, times(1)).deleteDoctorById(doctor.getId());
		}

		@Test
		 void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistDoctorDetails() {
			Doctor doctor = new Doctor();
			doctor.setId(99);
			when(doctordao.findDoctorById(doctor.getId())).thenReturn(Optional.empty());
			assertThrows(ResourceNotFoundException.class, () -> service.deleteDoctorById(doctor.getId()));
			verify(doctordao, times(1)).findDoctorById(doctor.getId());
			verify(doctordao, times(0)).deleteDoctorById(doctor.getId());
		}
	}

}

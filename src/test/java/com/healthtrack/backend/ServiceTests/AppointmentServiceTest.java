package com.healthtrack.backend.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.healthtrack.backend.DAO.AppointmentDAO;
import com.healthtrack.backend.DAO.DoctorDAO;
import com.healthtrack.backend.DAO.PatientDAO;
import com.healthtrack.backend.DTO.AppointmentRequest;
import com.healthtrack.backend.DTO.AppointmentUpdateRequest;
import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Appointment;
import com.healthtrack.backend.Entity.Doctor;
import com.healthtrack.backend.Entity.Patient;
import com.healthtrack.backend.Exception.ResourceNotFoundException;
import com.healthtrack.backend.Service.AppointmentService;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
	
	@InjectMocks
	private AppointmentService service;
	@Mock
	private AppointmentDAO appointmentdao;
	@Mock
	private PatientDAO patientdao;
	@Mock
	private DoctorDAO doctordao;
	
	
	@Nested
	class CreateTests{
		@Test
	    void shouldSaveAppointmentSuccessfully() {
	        AppointmentRequest request = new AppointmentRequest();
	        request.setDate(LocalDate.now());
	        request.setTime(LocalTime.now());

	        Patient patient = new Patient();
	        patient.setId(1);

	        Doctor doctor = new Doctor();
	        doctor.setId(1);

	        Appointment appointment = new Appointment();
	        appointment.setDate(request.getDate());
	        appointment.setTime(request.getTime());
	        appointment.setPatient(patient);
	        appointment.setDoctor(doctor);

	        when(patientdao.findPatientById(1)).thenReturn(Optional.of(patient));
	        when(doctordao.findDoctorById(1)).thenReturn(Optional.of(doctor));
	        when(appointmentdao.bookAppointment(any(Appointment.class))).thenReturn(appointment);

	        ResponseEntity<ResponseStructure<Appointment>> responseEntity =
	                service.bookAppointment(request, 1, 1);

	        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
	        assertEquals(appointment, responseEntity.getBody().getData());
	        verify(appointmentdao, times(1)).bookAppointment(any(Appointment.class));
	    }
		 @Test
		    void shouldThrowResourceNotFoundExceptionWhenPatientNotFound() {
		        AppointmentRequest request = new AppointmentRequest();
		        request.setDate(LocalDate.now());
		        request.setTime(LocalTime.now());

		        when(patientdao.findPatientById(1)).thenReturn(Optional.empty());

		        assertThrows(ResourceNotFoundException.class,
		                () -> service.bookAppointment(request, 1, 1));

		        verify(appointmentdao, never()).bookAppointment(any(Appointment.class));
		    }
		 @Test
		    void shouldThrowResourceNotFoundExceptionWhenDoctorNotFound() {
		        AppointmentRequest request = new AppointmentRequest();
		        request.setDate(LocalDate.now());
		        request.setTime(LocalTime.now());

		        Patient patient = new Patient();
		        patient.setId(1);

		        when(patientdao.findPatientById(1)).thenReturn(Optional.of(patient));
		        when(doctordao.findDoctorById(1)).thenReturn(Optional.empty());

		        assertThrows(ResourceNotFoundException.class,
		                () -> service.bookAppointment(request, 1, 1));

		        verify(appointmentdao, never()).bookAppointment(any(Appointment.class));
		    }
	}
	@Nested
	class  ReadTests{
		  @Test
		  void shouldReturnAllApointmentsSuccessfully() {
			   List<Appointment> list = Arrays.asList(new Appointment(),new Appointment());
			   when(appointmentdao.findAllAppointment())
			   .thenReturn(list);
			     ResponseEntity<ResponseStructure<List<Appointment>>> responseEntity = service.findAllAppointments();
			     
			  assertEquals(2, responseEntity.getBody().getData().size());
			  verify(appointmentdao,times(1)).findAllAppointment();
		  }
		  @Test
		  void shouldThrowResourceNotFoundExceptionWhenNoAppointments() {
			  when(appointmentdao.findAllAppointment())
			    .thenReturn(Collections.emptyList());
			  
              assertThrows(ResourceNotFoundException.class,()-> service.findAllAppointments());	
              
              verify(appointmentdao,times(1)).findAllAppointment();
		  }
		  
		  @Test
		  void shouldReturnAppointmentByIdSuccessfully() {
			   Appointment appointment=new Appointment();
			   appointment.setId(1);
			   when(appointmentdao.findAppointmentById(appointment.getId()))
			      .thenReturn(Optional.of(appointment));
			   ResponseEntity<ResponseStructure<Appointment>> responseEntity = service.findAppointmentById(appointment.getId());
			   
			   assertEquals(1, responseEntity.getBody().getData().getId());
			   verify(appointmentdao,times(1)).findAppointmentById(appointment.getId());
			      
		  }
		  @Test
		  void shouldThrowResourceNotFoundExceptionWhenAppointmentNonExists() {
			  when(appointmentdao.findAppointmentById(99))
			     .thenReturn(Optional.empty());
			     assertThrows(ResourceNotFoundException.class,()->service.findAppointmentById(99));
			     verify(appointmentdao,times(1)).findAppointmentById(99);
		  }
		  
	}
	
	@Nested
	class UpdateTests{
		@Test
	    void shouldUpdateAppointmentSuccessfully() {

	        Patient patient = new Patient();
	        patient.setId(1);

	        Doctor doctor = new Doctor();
	        doctor.setId(1);
	        AppointmentUpdateRequest updateRequest = new AppointmentUpdateRequest();
	        updateRequest.setDate(LocalDate.now().plusDays(1));
	        updateRequest.setTime(LocalTime.now().plusMinutes(1));
             updateRequest.setDoctorId(doctor.getId());
             updateRequest.setPatientId(patient.getId());

	        Appointment existingAppointment = new Appointment();
	        existingAppointment.setId(100); 
	        existingAppointment.setDate(LocalDate.now());
	        existingAppointment.setTime(LocalTime.now());
	        existingAppointment.setPatient(patient);
	        existingAppointment.setDoctor(doctor);

	        Appointment updatedAppointment = new Appointment();
	        updatedAppointment.setId(100);
	        updatedAppointment.setDate(updateRequest.getDate());
	        updatedAppointment.setTime(updateRequest.getTime());
	        updatedAppointment.setPatient(patient);
	        updatedAppointment.setDoctor(doctor);

	        when(appointmentdao.findAppointmentById(100)).thenReturn(Optional.of(existingAppointment));
	        when(patientdao.findPatientById(1)).thenReturn(Optional.of(patient));
	        when(doctordao.findDoctorById(1)).thenReturn(Optional.of(doctor));
	        when(appointmentdao.updateAppointment(any(Appointment.class))).thenReturn(updatedAppointment);

	        
	        ResponseEntity<ResponseStructure<Appointment>> responseEntity =
	                service.updateAppointment(updateRequest, 100);
            assertAll(
	          ()->     assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
	          ()->     assertEquals(updateRequest.getDate(), responseEntity.getBody().getData().getDate()),
	          ()->     assertEquals(updateRequest.getTime(), responseEntity.getBody().getData().getTime())
	        );

	        verify(appointmentdao, times(1)).updateAppointment(any(Appointment.class));
	        verify(patientdao, times(1)).findPatientById(1);
	        verify(doctordao, times(1)).findDoctorById(1);
	    }
		@Test
		void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistingAppointment() {
		    AppointmentUpdateRequest updateRequest = new AppointmentUpdateRequest();
		    updateRequest.setDate(LocalDate.now().plusDays(1));
		    updateRequest.setTime(LocalTime.now().plusMinutes(1));

		    when(appointmentdao.findAppointmentById(99)).thenReturn(Optional.empty());

		    assertThrows(ResourceNotFoundException.class,
		            () -> service.updateAppointment(updateRequest, 99));

		    verify(appointmentdao, never()).updateAppointment(any(Appointment.class));
		}

	}
	@Nested
	class DeleteTests{
		@Test
		void shouldDeleteAppointmentSuccessfully() {
			Appointment appointment=new Appointment();
			appointment.setId(1);
			when(appointmentdao.findAppointmentById(appointment.getId()))
			        .thenReturn(Optional.of(appointment));
			ResponseEntity<ResponseStructure<String>> responseEntity = service.deleteAppointmentById(appointment.getId());
			assertEquals(200, responseEntity.getBody().getStatusCode());
			verify(appointmentdao,times(1)).findAppointmentById(appointment.getId());
			verify(appointmentdao,times(1)).deleteAppointmentById(appointment.getId());
			
		}
		@Test
		void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistingAppointment() {
			when(appointmentdao.findAppointmentById(99))
			.thenReturn(Optional.empty());
			 assertThrows(ResourceNotFoundException.class, ()->service.deleteAppointmentById(99));
			 verify(appointmentdao,times(1)).findAppointmentById(99);
			 verify(appointmentdao,times(0)).deleteAppointmentById(99);
		}
	}
		
	}
	
	



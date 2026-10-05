package com.healthtrack.backend.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {

	private final AppointmentDAO appointmentdao;
	private final PatientDAO patientdao;
	private final DoctorDAO doctordao;

	private <T> ResponseEntity<ResponseStructure<T>> buildResponse(HttpStatus status, String message, T data) {
		ResponseStructure<T> response = new ResponseStructure<>();
		response.setStatusCode(status.value());
		response.setMessage(message);
		response.setData(data);
		return ResponseEntity.status(status).body(response);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<Appointment>> bookAppointment(AppointmentRequest request, Integer patientId,
			Integer doctorId) {
		Patient patient = patientdao.findPatientById(patientId)
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID " + patientId));
		

		Doctor doctor = doctordao.findDoctorById(doctorId)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID " + doctorId));
		
		if (request.getDate().isBefore(LocalDate.now())) {
		    throw new IllegalArgumentException("Cannot book appointment in the past");
		}


		Appointment appointment = new Appointment(); 
		appointment.setDate(request.getDate());
		appointment.setTime(request.getTime());
		appointment.setPatient(patient);
		appointment.setDoctor(doctor);

		Appointment savedAppointment = appointmentdao.bookAppointment(appointment);

		return buildResponse(
				HttpStatus.CREATED, "Appointment booked with Dr. " + doctor.getName() + " for patient "
						+ patient.getName() + " on " + savedAppointment.getDate() + " at " + savedAppointment.getTime(),
				savedAppointment);
	}
 
	public ResponseEntity<ResponseStructure<Appointment>> findAppointmentById(Integer appointmentId) {
		Appointment appointment = appointmentdao.findAppointmentById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID " + appointmentId));
		return buildResponse(HttpStatus.OK, "Appointment details found", appointment);
	}

	public ResponseEntity<ResponseStructure<List<Appointment>>> findAllAppointments() {
		List<Appointment> appointments = appointmentdao.findAllAppointment();
		if (appointments.isEmpty()) {
			throw new ResourceNotFoundException("No Appointment records found");
		}
		return buildResponse(HttpStatus.OK, "Appointment details found", appointments);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<Appointment>> updateAppointment(AppointmentUpdateRequest request,
			Integer appointmentId) {
		Appointment existingAppointment = appointmentdao.findAppointmentById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID " + appointmentId));

		Patient patient = patientdao.findPatientById(request.getPatientId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Patient not found with ID " + existingAppointment.getPatient().getId()));

		Doctor doctor = doctordao.findDoctorById(request.getDoctorId()).orElseThrow(() -> new ResourceNotFoundException(
				"Doctor not found with ID " + existingAppointment.getDoctor().getId()));

		existingAppointment.setDate(request.getDate());
		existingAppointment.setTime(request.getTime());
		existingAppointment.setPatient(patient);
		existingAppointment.setDoctor(doctor);

		Appointment updatedAppointment = appointmentdao.updateAppointment(existingAppointment);

		return buildResponse(HttpStatus.OK,
				"Appointment updated successfully for patient " + patient.getName() + " with Dr. " + doctor.getName()
						+ " on " + updatedAppointment.getDate() + " at " + updatedAppointment.getTime(),
				updatedAppointment);
	}

	public ResponseEntity<ResponseStructure<String>> deleteAppointmentById(Integer appointmentId) {
		appointmentdao.findAppointmentById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID " + appointmentId));
		appointmentdao.deleteAppointmentById(appointmentId);
		return buildResponse(HttpStatus.OK, "Appointment deleted successfully with ID " + appointmentId, null);
	}
	
	
	
	public ResponseEntity<ResponseStructure<List<Appointment>>> getAppointmentsByDoctorId(Integer doctorId) {
	    doctordao.findDoctorById(doctorId)
	            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID " + doctorId));

	    List<Appointment> appointments = appointmentdao.getAppointmentsByDoctorId(doctorId);

	    String message = appointments.isEmpty() ? "No appointments found" : "Appointment details found";
	    return buildResponse(HttpStatus.OK, message, appointments);
	}

	public ResponseEntity<ResponseStructure<Patient>> getPatientByAppointmentId(Integer appointmentId) {
		               Patient patient = appointmentdao.getPatientByAppointmentId(appointmentId)
		               .orElseThrow(()->new ResourceNotFoundException("Patient not found with apoointmentId"+appointmentId));
		               
		
		return buildResponse(HttpStatus.OK, "patient details found", patient);
	}

	public ResponseEntity<ResponseStructure<List<Appointment>>> getAppointmentsByPatientId(Integer patientId) {
		          List<Appointment> existedAppointments = appointmentdao.getAppointmentsByPatientId(patientId);
		      if(existedAppointments.isEmpty())
		    	  throw new ResourceNotFoundException("Appointment not found with patientId"+patientId);
		      
		      return buildResponse(HttpStatus.OK, "appointment details found", existedAppointments);
	}

	
}

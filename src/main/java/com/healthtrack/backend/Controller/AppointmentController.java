package com.healthtrack.backend.Controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthtrack.backend.DTO.AppointmentRequest;
import com.healthtrack.backend.DTO.AppointmentUpdateRequest;
import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Appointment;
import com.healthtrack.backend.Entity.Patient;
import com.healthtrack.backend.Service.AppointmentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointment", description = "Appointment related API's")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {
	@Autowired
	private AppointmentService service;

	@PostMapping("/patient/{patientId}/doctor/{doctorId}")
	public ResponseEntity<ResponseStructure<Appointment>> bookAppointment(
			@Valid @RequestBody AppointmentRequest request, @PathVariable Integer patientId,
			@PathVariable Integer doctorId) {
		return service.bookAppointment(request, patientId, doctorId);
	}

	@GetMapping("/{appointmentId}")
	public ResponseEntity<ResponseStructure<Appointment>> findAppointmentById(@PathVariable Integer appointmentId) {
		return service.findAppointmentById(appointmentId);
	}

	@GetMapping
	public ResponseEntity<ResponseStructure<List<Appointment>>> findAllAppointment() {
		return service.findAllAppointments();
	}

	@PutMapping("/{appointmentId}")
	public ResponseEntity<ResponseStructure<Appointment>> updateAppointment(
			@Valid @RequestBody AppointmentUpdateRequest request, @PathVariable Integer appointmentId) {
		return service.updateAppointment(request, appointmentId);
	}

	@DeleteMapping("/{appointmentId}")
	public ResponseEntity<ResponseStructure<String>> deleteAppointmentById(@PathVariable Integer appointmentId) {
		return service.deleteAppointmentById(appointmentId);
	}

	// get  patient by appointmentId
	@GetMapping("/patients/{appointmentId}")
	public ResponseEntity<ResponseStructure<Patient>> getPatientByAppointmentId(
			@PathVariable Integer appointmentId) {
		return service.getPatientByAppointmentId(appointmentId);
	}

	// get all appointment by doctorId
	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<ResponseStructure<List<Appointment>>> getAppointmentsByDoctorId(
			@PathVariable Integer doctorId) {
		return service.getAppointmentsByDoctorId(doctorId);
	}
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<ResponseStructure<List<Appointment>>> getAppointmentsByPatientId(
			@PathVariable Integer patientId) {
		return service.getAppointmentsByPatientId(patientId);
	}
	
}

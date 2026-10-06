package com.healthtrack.backend.Controller;

import java.util.List;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Patient;
import com.healthtrack.backend.Service.PatientService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/patient")
@Tag(name = "Patient",description = "Patient related API's")
@CrossOrigin(origins = { "http://localhost:3000", "https://health-track-management-system.vercel.app" })
@RequiredArgsConstructor
public class PatientController {
	private final PatientService service;

	
	@GetMapping("/{patientId}")
	public ResponseEntity<ResponseStructure<Patient>> findPatientById( @PathVariable  Integer patientId){
		return service.findPatientById(patientId);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Patient>>> findAllPatient(){
		return service.findAllPatients();
	}
	@PutMapping("/{patientId}")
	public ResponseEntity<ResponseStructure<Patient>> updatePatient(@Valid @RequestBody Patient patient,@PathVariable Integer patientId){
		return service.updatePatient(patient,patientId);
	}
	
	@DeleteMapping("/{patientId}")
	public ResponseEntity<ResponseStructure<String>> deletePatientById(  @PathVariable Integer patientId){
		return service.deletePatientById(patientId);
	}
	
	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<ResponseStructure<List<Patient>>> findPatientsByDoctorId(@PathVariable Integer doctorId){
		return service.findPatientsByDoctorId(doctorId);
	}
	@GetMapping("/email/{email}")
	public ResponseEntity<ResponseStructure<Patient>>  findPatientByEmail ( @PathVariable String  email){
		return service.findPatientByEmail(email);
	}
	

}

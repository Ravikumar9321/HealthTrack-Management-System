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

import com.healthtrack.backend.DTO.PrescriptionRequest;
import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Prescription;
import com.healthtrack.backend.Service.PrescriptionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prescription")
@Tag(name = "Prescription",description = "Prescription related API's")
@CrossOrigin(origins = { "http://localhost:3000", "https://health-track-management-system.vercel.app" })
public class PrescriptionController {
	@Autowired
	private PrescriptionService service;

	@PostMapping("/appointment/{appointmentId}")
	public ResponseEntity<ResponseStructure<Prescription>> addPrescription(@Valid @RequestBody PrescriptionRequest request,@PathVariable Integer appointmentId){
		return service.addPrescription(request,appointmentId);
	}
	
	@GetMapping("/{prescriptionId}")
	public ResponseEntity<ResponseStructure<Prescription>> findPrescriptionById( @PathVariable  Integer prescriptionId){
		return service.findPrescriptionById(prescriptionId);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Prescription>>> findAllPrescription(){
		return service.findAllPrescriptions();
	}
	@PutMapping("/{prescriptionId}")
	public ResponseEntity<ResponseStructure<Prescription>> updatePrescription(@Valid @RequestBody PrescriptionRequest request,@PathVariable Integer prescriptionId){
		return service.updatePrescription(request,prescriptionId);
	}
	
	@DeleteMapping("/{prescriptionId}")
	public ResponseEntity<ResponseStructure<String>> deletePrescriptionById(  @PathVariable Integer prescriptionId){
		return service.deletePrescriptionById(prescriptionId);
	}
	
	@GetMapping("/appointment/{appointmentId}")
	public ResponseEntity<ResponseStructure<Prescription>> findPrescriptionByAppointmentId( @PathVariable  Integer appointmentId){
		return service.findPrescriptionByAppointmentId(appointmentId);
	}
	

}

package com.healthtrack.backend.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthtrack.backend.DAO.AppointmentDAO;
import com.healthtrack.backend.DAO.PrescriptionDAO;
import com.healthtrack.backend.DTO.PrescriptionRequest;
import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Appointment;
import com.healthtrack.backend.Entity.Prescription;
import com.healthtrack.backend.Exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

	private final PrescriptionDAO prescriptiondao;
	private final AppointmentDAO appointmentdao;

	private <T> ResponseEntity<ResponseStructure<T>> buildResponse(HttpStatus status, String message, T data) {
		ResponseStructure<T> response = new ResponseStructure<>();
		response.setStatusCode(status.value());
		response.setMessage(message);
		response.setData(data);
		return ResponseEntity.status(status).body(response);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<Prescription>> addPrescription(PrescriptionRequest request,
	        Integer appointmentId) {

	    Appointment appointment = appointmentdao.findAppointmentById(appointmentId)
	            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID " + appointmentId));

	    Optional<Prescription> existingPrescription = prescriptiondao.findPrescriptionByAppointmentId(appointmentId);
	    if (existingPrescription.isPresent()) {
	        return buildResponse(HttpStatus.CONFLICT,
	                "Prescription already exists for appointmentId " + appointmentId + ". Please use updatePrescription instead.",
	                null);
	    }

	    Prescription prescription = new Prescription();
	    prescription.setAppointment(appointment);
	    prescription.setMedicines(request.getMedicines());
	    prescription.setDosage(request.getDosage());
	    prescription.setNotes(request.getNotes());

	    Prescription savedPrescription = prescriptiondao.addPrescription(prescription);

	    return buildResponse(HttpStatus.CREATED,
	            "Prescription created successfully for patient " + appointment.getPatient().getName() +
	            " with Dr. " + appointment.getDoctor().getName(),
	            savedPrescription);
	}

	public ResponseEntity<ResponseStructure<Prescription>> findPrescriptionById(Integer prescriptionId) {
		Prescription prescription = prescriptiondao.findPrescriptionById(prescriptionId)
				.orElseThrow(() -> new ResourceNotFoundException("Prescription not found with ID " + prescriptionId));
		return buildResponse(HttpStatus.OK, "Prescription details found", prescription);
	}

	public ResponseEntity<ResponseStructure<List<Prescription>>> findAllPrescriptions() {
		List<Prescription> prescriptions = prescriptiondao.findAllPrescription();
		if (prescriptions.isEmpty()) {
			throw new ResourceNotFoundException("No Prescription records found");
		}
		return buildResponse(HttpStatus.OK, "Prescription details found", prescriptions);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<Prescription>> updatePrescription(PrescriptionRequest request,
	        Integer prescriptionId) {

	    Prescription existingPrescription = prescriptiondao.findPrescriptionById(prescriptionId)
	            .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with ID " + prescriptionId));
	    
	    existingPrescription.setDosage(request.getDosage());
	    existingPrescription.setMedicines(request.getMedicines());
	    existingPrescription.setNotes(request.getNotes());
	    Prescription updatedPrescription = prescriptiondao.updatePrescription(existingPrescription);

	    return buildResponse(HttpStatus.OK, "Prescription updated successfully", updatedPrescription);
	}
	
	@Transactional
	public ResponseEntity<ResponseStructure<String>> deletePrescriptionById(Integer prescriptionId) {
	    Prescription prescription = prescriptiondao.findPrescriptionById(prescriptionId)
	        .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with ID " + prescriptionId));

	    Appointment appointment = prescription.getAppointment();
	    if (appointment != null) {
	        appointment.setPrescription(null);
	    }

	    prescriptiondao.deletePrescriptionById(prescriptionId);

	    return buildResponse(HttpStatus.OK,
	        "Prescription deleted successfully with ID " + prescriptionId, null);
	}

	public ResponseEntity<ResponseStructure<Prescription>> findPrescriptionByAppointmentId(Integer appointmentId) {
		Prescription prescription = prescriptiondao.findPrescriptionByAppointmentId(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Prescription not found with appointementId " + appointmentId));
		
	
		return buildResponse(HttpStatus.OK, "Prescription details found", prescription);
	}

}

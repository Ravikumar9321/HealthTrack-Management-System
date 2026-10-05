package com.healthtrack.backend.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthtrack.backend.DAO.AppointmentDAO;
import com.healthtrack.backend.DAO.BillingDAO;
import com.healthtrack.backend.DTO.BillingRequest;
import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Appointment;
import com.healthtrack.backend.Entity.Billing;
import com.healthtrack.backend.Exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillingService {

	private final BillingDAO billingdao;
	private final AppointmentDAO apppointmentdao;

	private <T> ResponseEntity<ResponseStructure<T>> buildResponse(HttpStatus status, String message, T data) {
		ResponseStructure<T> response = new ResponseStructure<>();
		response.setStatusCode(status.value());
		response.setMessage(message);
		response.setData(data);
		return ResponseEntity.status(status).body(response);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<Billing>> generateBilling(BillingRequest request, Integer appointmentId) {
		Appointment appointment = apppointmentdao.findAppointmentById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with" + appointmentId));

		if (appointment.getBilling() != null) {
			throw new IllegalStateException("Billing already exists for this appointment. Use updateBilling instead.");
		}
           if(appointment.getPrescription()==null) 
       	    throw new ResourceNotFoundException("Prescription not found cannot able to generate bill for appointId"+appointmentId);

           
		Billing billing = new Billing();
		billing.setAppointment(appointment);
		billing.setAmount(request.getAmount());
		billing.setStatus(request.getStatus());

		Billing savedBilling = billingdao.generateBilling(billing);
		return buildResponse(HttpStatus.CREATED,
				"Billing generated successfully for Appointment ID " 
						+ appointment.getId() 
						+ " (Patient: " + appointment.getPatient().getName() 
						+ ", Status: " + savedBilling.getStatus() + ")",
				savedBilling);
	}

	public ResponseEntity<ResponseStructure<Billing>> findBillingById(Integer billingId) {
		Billing billing = billingdao.findBillingById(billingId)
				.orElseThrow(() -> new ResourceNotFoundException("Billing not found with ID " + billingId));
		return buildResponse(HttpStatus.OK, "Billing details found", billing);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<Billing>> updateBilling(BillingRequest request, Integer billingId) {
		Billing existedBilling = billingdao.findBillingById(billingId)
				.orElseThrow(() -> new ResourceNotFoundException("Billing not found with ID " + billingId));
		existedBilling.setAmount(request.getAmount());
		existedBilling.setStatus(request.getStatus());
		Billing updatedBilling = billingdao.updateBilling(existedBilling);
		return buildResponse(HttpStatus.OK, "Billing updated successfully", updatedBilling);
	}

	@Transactional
	public ResponseEntity<ResponseStructure<String>> deleteBillingById(Integer billingId) {
		billingdao.findBillingById(billingId)
				.orElseThrow(() -> new ResourceNotFoundException("Billing not found with ID " + billingId));
		billingdao.deleteBillingById(billingId);
		return buildResponse(HttpStatus.OK, "Billing deleted successfully with ID " + billingId, null);
	}

	public ResponseEntity<ResponseStructure<Billing>> findBillingDetailstByAppointmentId(Integer appointmentId) {
		apppointmentdao.findAppointmentById(appointmentId)
		.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID " + appointmentId));
		
		Billing exitedBilling = billingdao.findBillingDetailstByAppointmentId(appointmentId)
		.orElseThrow(()->new ResourceNotFoundException("billing not found with appointmentId"+appointmentId));
		return buildResponse(HttpStatus.OK, "bill details founded", exitedBilling);
	}
}

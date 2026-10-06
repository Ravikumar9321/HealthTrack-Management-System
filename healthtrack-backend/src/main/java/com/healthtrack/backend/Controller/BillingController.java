package com.healthtrack.backend.Controller;


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

import com.healthtrack.backend.DTO.BillingRequest;
import com.healthtrack.backend.DTO.ResponseStructure;
import com.healthtrack.backend.Entity.Billing;
import com.healthtrack.backend.Service.BillingService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/billing")
@Tag(name = "Billing",description = "Billing related API's")
@CrossOrigin(origins = { "http://localhost:3000", "https://health-track-management-system.vercel.app" })
public class BillingController {
	@Autowired
	private BillingService service;

	@PostMapping("/appointment/{appointmentId}")
	public ResponseEntity<ResponseStructure<Billing>> generateBilling(@Valid @RequestBody BillingRequest request,@PathVariable Integer appointmentId){
		return service.generateBilling(request,appointmentId);
	}
	
	@GetMapping("/{billingId}")
	public ResponseEntity<ResponseStructure<Billing>> findBillingById( @PathVariable  Integer billingId){
		return service.findBillingById(billingId);
	}
	
	
	@PutMapping("/{billingId}")
	public ResponseEntity<ResponseStructure<Billing>> updateBilling(@Valid @RequestBody BillingRequest billing,@PathVariable Integer billingId){
		return service.updateBilling(billing,billingId);
	}
	
	@DeleteMapping("/{billingId}")
	public ResponseEntity<ResponseStructure<String>> deleteBillingById(  @PathVariable Integer billingId){
		return service.deleteBillingById(billingId);
	}
	
	@GetMapping("/appointment/{appointmentId}")
	public ResponseEntity<ResponseStructure<Billing>> findBillingDetailstByAppointmentId( @PathVariable  Integer appointmentId){
		return service.findBillingDetailstByAppointmentId(appointmentId);
	}
	

}

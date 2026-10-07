package com.healthtrack.backend.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.healthtrack.backend.Entity.Billing;
import com.healthtrack.backend.Repository.BillingRepository;

@Repository
public class BillingDAO {
	@Autowired
	private BillingRepository repository;

	public Billing generateBilling(Billing billing) {
        		return repository.save(billing);
	}

	public Optional<Billing> findBillingById(Integer billingId) {
           return repository.findById(billingId);	
	}

	public List<Billing> findAllBilling() {
         return repository.findAll();		
	}

	public Billing updateBilling(Billing billing) {
     return repository.save(billing);		
	}

	public void deleteBillingById(Integer billingId) {
        repository.deleteById(billingId);		
	}

	public Optional<Billing> findBillingDetailstByAppointmentId(Integer appointmentId) {
		return repository.findBillingDetailstByAppointmentId(appointmentId);
		
	}

	

}

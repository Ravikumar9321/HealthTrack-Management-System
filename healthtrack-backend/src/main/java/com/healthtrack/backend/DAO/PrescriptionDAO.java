package com.healthtrack.backend.DAO;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.healthtrack.backend.Entity.Prescription;
import com.healthtrack.backend.Repository.PrescriptionRepository;
@Repository
public class PrescriptionDAO {

	@Autowired
	private PrescriptionRepository repository;

	public Prescription addPrescription(Prescription prescription) {
        		return repository.save(prescription);
	}

	public Optional<Prescription> findPrescriptionById(Integer prescriptionId) {
           return repository.findById(prescriptionId);	
	}

	public List<Prescription> findAllPrescription() {
         return repository.findAll();		
	}

	public Prescription updatePrescription(Prescription prescription) {
     return repository.save(prescription);		
	}

	public void deletePrescriptionById(Integer prescriptionId) {
        repository.deleteById(prescriptionId);		
	}

	
	public Optional<Prescription> findPrescriptionByAppointmentId(Integer appointmentId) {
	
    return repository.findPrescriptionByAppointmentId(appointmentId);

}
}

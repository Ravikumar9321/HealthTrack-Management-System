package com.healthtrack.backend.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.healthtrack.backend.Entity.Patient;
import com.healthtrack.backend.Repository.PatientRepository;

@Repository
public class PatientDAO {
	@Autowired
	private PatientRepository repository;

	public Patient registerPatient(Patient patient) {
        		return repository.save(patient);
	}

	public Optional<Patient> findPatientById(Integer patientId) {
           return repository.findById(patientId);	
	}

	public List<Patient> findAllPatient() {
         return repository.findAll();		
	}

	public Patient updatePatient(Patient patient) {
     return repository.save(patient);		
	}

	public void deletePatientById(Integer patientId) {
        repository.deleteById(patientId);		
	}

	public List<Patient> findPatientsByDoctorId(Integer doctorId) {
		return repository.findPatientsByDoctorId(doctorId);
	}

	public Optional<Patient> findPatientByEmail(String email) {
		return repository.findPatientByEmail(email);
	}

}

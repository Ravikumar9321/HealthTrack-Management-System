package com.healthtrack.backend.DAO;

import java.util.List;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.healthtrack.backend.Entity.Doctor;
import com.healthtrack.backend.Repository.DoctorRepository;

@Repository
public class DoctorDAO {
	@Autowired
	private DoctorRepository repository;

	public Doctor registerDoctor(Doctor doctor) {
        		return repository.save(doctor);
	}

	public Optional<Doctor> findDoctorById(Integer doctorId) {
           return repository.findById(doctorId);	
	}

	public List<Doctor> findAllDoctor() {
         return repository.findAll();		
	}

	public Doctor updateDoctor(Doctor doctor) {
     return repository.save(doctor);		
	}

	public void deleteDoctorById(Integer doctorId) {
        repository.deleteById(doctorId);		
	}

	public Optional<Doctor> findDoctorByEmail(String email) {
		return   repository.findDoctorByEmail(email);
		
	}

}

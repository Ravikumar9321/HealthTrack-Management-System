package com.healthtrack.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.healthtrack.backend.Entity.Patient;

public interface PatientRepository  extends JpaRepository<Patient, Integer>{

	Optional<Patient> findByEmail(String email);
	
	@Query("select distinct p from Patient p join p.appointments a where a.doctor.id = ?1")
	List<Patient> findPatientsByDoctorId(Integer doctorId);

	Optional<Patient> findPatientByEmail(String email);

}
